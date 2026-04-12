package io.ruin.model.map.route;

import io.ruin.model.map.ClipUtils;

import static io.ruin.model.map.ClipConstants.*;

public class RouteCalculator {
	/**
	 * Route finding
	 * shadowrs: these can be static, expected usage is to only run on single thread (game thread) and used by one entity at a time, no conflicts
	 */

	private static final int[][] directions = new int[128][128];
	private static final int[][] distances = new int[128][128];
	private static final int[] queueX = new int[4096];
	private static final int[] queueY = new int[4096];
	private static int foundMapX;
	private static int foundMapY;

	public static int findRoute(int fromMapX, int fromMapY, int size, RouteType route, ClipUtils clipUtils, boolean findAlternative, int[] stepsX, int[] stepsY) {
		for (int x = 0; x < 128; x++) {
			for (int y = 0; y < 128; y++) {
				directions[x][y] = 0;
				distances[x][y] = 99999999;
			}
		}
		route.localize(clipUtils);

		if (size == 1)
			route.reachable = findRoute1(fromMapX, fromMapY, route, clipUtils);
		else if (size == 2)
			route.reachable = findRoute2(fromMapX, fromMapY, route, clipUtils);
		else
			route.reachable = findRouteXAlternative(fromMapX, fromMapY, size, route, clipUtils);

		int arrayOffsetX = fromMapX - 64;
		int arrayOffsetY = fromMapY - 64;
		int foundMapX = RouteCalculator.foundMapX;
		int foundMapY = RouteCalculator.foundMapY;
		if (!route.reachable) {
			if (!findAlternative)
				return -1;
			int lowestCost = 2147483647;
			int lowestDistance = 2147483647;
			int checkRange = 10;
			int toMapX = route.x;
			int toMapY = route.y;
			int toSizeX = route.xLength;
			int toSizeY = route.yLength;
			for (int checkMapX = toMapX - checkRange; checkMapX <= toMapX + checkRange; checkMapX++) {
				for (int checkMapY = toMapY - checkRange; checkMapY <= checkRange + toMapY; checkMapY++) {
					int arrayX = checkMapX - arrayOffsetX;
					int arrayY = checkMapY - arrayOffsetY;
					if (arrayX >= 0 && arrayY >= 0 && arrayX < 128 && arrayY < 128 && (distances[arrayX][arrayY] < 100)) {
						int deltaX = 0;
						if (checkMapX < toMapX)
							deltaX = toMapX - checkMapX;
						else if (checkMapX > toSizeX + toMapX - 1)
							deltaX = checkMapX - (toSizeX + toMapX - 1);
						int deltaY = 0;
						if (checkMapY < toMapY)
							deltaY = toMapY - checkMapY;
						else if (checkMapY > toSizeY + toMapY - 1)
							deltaY = checkMapY - (toMapY + toSizeY - 1);
						int cost = deltaY * deltaY + deltaX * deltaX;
						if (cost < lowestCost || (lowestCost == cost && (distances[arrayX][arrayY]) < lowestDistance)) {
							lowestCost = cost;
							lowestDistance = (distances[arrayX][arrayY]);
							foundMapX = checkMapX;
							foundMapY = checkMapY;
						}
					}
				}
			}
			if (2147483647 == lowestCost)
				return -1;
		}
		if (foundMapX == fromMapX && fromMapY == foundMapY)
			return 0;
		int writeOffset = 0;
		queueX[writeOffset] = foundMapX;
		queueY[writeOffset++] = foundMapY;
		int lastWrittenDirection;
		int direction = lastWrittenDirection = (directions[foundMapX - arrayOffsetX][foundMapY - arrayOffsetY]);
		while (foundMapX != fromMapX || fromMapY != foundMapY) {
			if (lastWrittenDirection != direction) {
				lastWrittenDirection = direction;
				queueX[writeOffset] = foundMapX;
				queueY[writeOffset++] = foundMapY;
			}
			if ((direction & 0x2) != 0)
				foundMapX++;
			else if ((direction & 0x8) != 0)
				foundMapX--;
			if ((direction & 0x1) != 0)
				foundMapY++;
			else if ((direction & 0x4) != 0)
				foundMapY--;
			direction = (directions[foundMapX - arrayOffsetX][foundMapY - arrayOffsetY]);
		}
		int stepCount = 0;
		while (writeOffset-- > 0) {
			stepsX[stepCount] = clipUtils.baseX + queueX[writeOffset];
			stepsY[stepCount] = clipUtils.baseY + queueY[writeOffset];
			if (++stepCount >= stepsX.length)
				break;
		}
		return stepCount;
	}

	private static boolean findRoute1(int fromMapX, int fromMapY, RouteType route, ClipUtils clipUtils) {
		int currentMapX = fromMapX;
		int currentMapY = fromMapY;
		int currentArrayOffsetX = 64;
		int currentArrayOffsetY = 64;
		int baseArrayOffsetX = fromMapX - currentArrayOffsetX;
		int baseArrayOffsetY = fromMapY - currentArrayOffsetY;
		directions[currentArrayOffsetX][currentArrayOffsetY] = 99;
		distances[currentArrayOffsetX][currentArrayOffsetY] = 0;
		int writeOffset = 0;
		int readOffset = 0;
		queueX[writeOffset] = fromMapX;
		queueY[writeOffset++] = fromMapY;
		while (readOffset != writeOffset) {
			currentMapX = queueX[readOffset];
			currentMapY = queueY[readOffset];
			readOffset = readOffset + 1 & 0xfff;
			currentArrayOffsetX = currentMapX - baseArrayOffsetX;
			currentArrayOffsetY = currentMapY - baseArrayOffsetY;
			int currentClipMapX = currentMapX;
			int currentClipMapY = currentMapY;
			if (route.method4274(1, currentMapX, currentMapY, clipUtils)) {
				foundMapX = currentMapX;
				foundMapY = currentMapY;
				return true;
			}
			int distance = distances[currentArrayOffsetX][currentArrayOffsetY] + 1;
			if (currentArrayOffsetX > 0 && directions[currentArrayOffsetX - 1][currentArrayOffsetY] == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY) & WEST_MASK) == 0) {
				queueX[writeOffset] = currentMapX - 1;
				queueY[writeOffset] = currentMapY;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX - 1][currentArrayOffsetY] = 2;
				distances[currentArrayOffsetX - 1][currentArrayOffsetY] = distance;
			}
			if (currentArrayOffsetX < 127 && directions[currentArrayOffsetX + 1][currentArrayOffsetY] == 0 && (clipUtils.get(currentClipMapX + 1, currentClipMapY) & EAST_MASK) == 0) {
				queueX[writeOffset] = currentMapX + 1;
				queueY[writeOffset] = currentMapY;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX + 1][currentArrayOffsetY] = 8;
				distances[currentArrayOffsetX + 1][currentArrayOffsetY] = distance;
			}
			if (currentArrayOffsetY > 0 && directions[currentArrayOffsetX][currentArrayOffsetY - 1] == 0 && (clipUtils.get(currentClipMapX, currentClipMapY - 1) & SOUTH_MASK) == 0) {
				queueX[writeOffset] = currentMapX;
				queueY[writeOffset] = currentMapY - 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX][currentArrayOffsetY - 1] = 1;
				distances[currentArrayOffsetX][currentArrayOffsetY - 1] = distance;
			}
			if (currentArrayOffsetY < 127 && directions[currentArrayOffsetX][currentArrayOffsetY + 1] == 0 && (clipUtils.get(currentClipMapX, currentClipMapY + 1) & NORTH_MASK) == 0) {
				queueX[writeOffset] = currentMapX;
				queueY[writeOffset] = currentMapY + 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX][currentArrayOffsetY + 1] = 4;
				distances[currentArrayOffsetX][currentArrayOffsetY + 1] = distance;
			}
			if (currentArrayOffsetX > 0 && currentArrayOffsetY > 0 && directions[currentArrayOffsetX - 1][currentArrayOffsetY - 1] == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY - 1) & SOUTH_WEST_MASK) == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY) & WEST_MASK) == 0 && (clipUtils.get(currentClipMapX, currentClipMapY - 1) & SOUTH_MASK) == 0) {
				queueX[writeOffset] = currentMapX - 1;
				queueY[writeOffset] = currentMapY - 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX - 1][currentArrayOffsetY - 1] = 3;
				distances[currentArrayOffsetX - 1][currentArrayOffsetY - 1] = distance;
			}
			if (currentArrayOffsetX < 127 && currentArrayOffsetY > 0 && directions[currentArrayOffsetX + 1][currentArrayOffsetY - 1] == 0 && (clipUtils.get(currentClipMapX + 1, currentClipMapY - 1) & SOUTH_EAST_MASK) == 0 && (clipUtils.get(currentClipMapX + 1, currentClipMapY) & EAST_MASK) == 0 && (clipUtils.get(currentClipMapX, currentClipMapY - 1) & SOUTH_MASK) == 0) {
				queueX[writeOffset] = currentMapX + 1;
				queueY[writeOffset] = currentMapY - 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX + 1][currentArrayOffsetY - 1] = 9;
				distances[currentArrayOffsetX + 1][currentArrayOffsetY - 1] = distance;
			}
			if (currentArrayOffsetX > 0 && currentArrayOffsetY < 127 && directions[currentArrayOffsetX - 1][currentArrayOffsetY + 1] == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY + 1) & NORTH_WEST_MASK) == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY) & WEST_MASK) == 0 && (clipUtils.get(currentClipMapX, currentClipMapY + 1) & NORTH_MASK) == 0) {
				queueX[writeOffset] = currentMapX - 1;
				queueY[writeOffset] = currentMapY + 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX - 1][currentArrayOffsetY + 1] = 6;
				distances[currentArrayOffsetX - 1][currentArrayOffsetY + 1] = distance;
			}
			if (currentArrayOffsetX < 127 && currentArrayOffsetY < 127 && directions[currentArrayOffsetX + 1][currentArrayOffsetY + 1] == 0 && (clipUtils.get(currentClipMapX + 1, currentClipMapY + 1) & NORTH_EAST_MASK) == 0 && (clipUtils.get(currentClipMapX + 1, currentClipMapY) & EAST_MASK) == 0 && (clipUtils.get(currentClipMapX, currentClipMapY + 1) & NORTH_MASK) == 0) {
				queueX[writeOffset] = currentMapX + 1;
				queueY[writeOffset] = currentMapY + 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX + 1][currentArrayOffsetY + 1] = 12;
				distances[currentArrayOffsetX + 1][currentArrayOffsetY + 1] = distance;
			}
		}
		foundMapX = currentMapX;
		foundMapY = currentMapY;
		return false;
	}

	private static boolean findRoute2(int fromMapX, int fromMapY, RouteType route, ClipUtils clipUtils) {
		int currentMapX = fromMapX;
		int currentMapY = fromMapY;
		int currentArrayOffsetX = 64;
		int currentArrayOffsetY = 64;
		int baseArrayOffsetX = fromMapX - currentArrayOffsetX;
		int baseArrayOffsetY = fromMapY - currentArrayOffsetY;
		directions[currentArrayOffsetX][currentArrayOffsetY] = 99;
		distances[currentArrayOffsetX][currentArrayOffsetY] = 0;
		int writeOffset = 0;
		int readOffset = 0;
		queueX[writeOffset] = fromMapX;
		queueY[writeOffset++] = fromMapY;
		while (readOffset != writeOffset) {
			currentMapX = queueX[readOffset];
			currentMapY = queueY[readOffset];
			readOffset = readOffset + 1 & 0xfff;
			currentArrayOffsetX = currentMapX - baseArrayOffsetX;
			currentArrayOffsetY = currentMapY - baseArrayOffsetY;
			int currentClipMapX = currentMapX;
			int currentClipMapY = currentMapY;
			if (route.method4274(2, currentMapX, currentMapY, clipUtils)) {
				foundMapX = currentMapX;
				foundMapY = currentMapY;
				return true;
			}
			int distance = distances[currentArrayOffsetX][currentArrayOffsetY] + 1;
			if (currentArrayOffsetX > 0 && (directions[currentArrayOffsetX - 1][currentArrayOffsetY]) == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY) & SOUTH_WEST_MASK) == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY + 1) & NORTH_WEST_MASK) == 0) {
				queueX[writeOffset] = currentMapX - 1;
				queueY[writeOffset] = currentMapY;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX - 1][currentArrayOffsetY] = 2;
				distances[currentArrayOffsetX - 1][currentArrayOffsetY] = distance;
			}
			if (currentArrayOffsetX < 126 && (directions[currentArrayOffsetX + 1][currentArrayOffsetY]) == 0 && (clipUtils.get(currentClipMapX + 2, currentClipMapY) & SOUTH_EAST_MASK) == 0 && (clipUtils.get(currentClipMapX + 2, currentClipMapY + 1) & NORTH_EAST_MASK) == 0) {
				queueX[writeOffset] = currentMapX + 1;
				queueY[writeOffset] = currentMapY;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX + 1][currentArrayOffsetY] = 8;
				distances[currentArrayOffsetX + 1][currentArrayOffsetY] = distance;
			}
			if (currentArrayOffsetY > 0 && (directions[currentArrayOffsetX][currentArrayOffsetY - 1]) == 0 && (clipUtils.get(currentClipMapX, currentClipMapY - 1) & SOUTH_WEST_MASK) == 0 && (clipUtils.get(currentClipMapX + 1, currentClipMapY - 1) & SOUTH_EAST_MASK) == 0) {
				queueX[writeOffset] = currentMapX;
				queueY[writeOffset] = currentMapY - 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX][currentArrayOffsetY - 1] = 1;
				distances[currentArrayOffsetX][currentArrayOffsetY - 1] = distance;
			}
			if (currentArrayOffsetY < 126 && (directions[currentArrayOffsetX][currentArrayOffsetY + 1]) == 0 && (clipUtils.get(currentClipMapX, currentClipMapY + 2) & NORTH_WEST_MASK) == 0 && (clipUtils.get(currentClipMapX + 1, currentClipMapY + 2) & NORTH_EAST_MASK) == 0) {
				queueX[writeOffset] = currentMapX;
				queueY[writeOffset] = currentMapY + 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX][currentArrayOffsetY + 1] = 4;
				distances[currentArrayOffsetX][currentArrayOffsetY + 1] = distance;
			}
			if (currentArrayOffsetX > 0 && currentArrayOffsetY > 0 && (directions[currentArrayOffsetX - 1][currentArrayOffsetY - 1]) == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY) & 0x124013e) == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY - 1) & SOUTH_WEST_MASK) == 0 && (clipUtils.get(currentClipMapX, currentClipMapY - 1) & 0x124018f) == 0) {
				queueX[writeOffset] = currentMapX - 1;
				queueY[writeOffset] = currentMapY - 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX - 1][currentArrayOffsetY - 1] = 3;
				distances[currentArrayOffsetX - 1][currentArrayOffsetY - 1] = distance;
			}
			if (currentArrayOffsetX < 126 && currentArrayOffsetY > 0 && (directions[currentArrayOffsetX + 1][currentArrayOffsetY - 1]) == 0 && (clipUtils.get(currentClipMapX + 1, currentClipMapY - 1) & 0x124018f) == 0 && (clipUtils.get(currentClipMapX + 2, currentClipMapY - 1) & SOUTH_EAST_MASK) == 0 && (clipUtils.get(currentClipMapX + 2, currentClipMapY) & 0x12401e3) == 0) {
				queueX[writeOffset] = currentMapX + 1;
				queueY[writeOffset] = currentMapY - 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX + 1][currentArrayOffsetY - 1] = 9;
				distances[currentArrayOffsetX + 1][currentArrayOffsetY - 1] = distance;
			}
			if (currentArrayOffsetX > 0 && currentArrayOffsetY < 126 && (directions[currentArrayOffsetX - 1][currentArrayOffsetY + 1]) == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY + 1) & 0x124013e) == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY + 2) & NORTH_WEST_MASK) == 0 && (clipUtils.get(currentClipMapX, currentClipMapY + 2) & 0x12401f8) == 0) {
				queueX[writeOffset] = currentMapX - 1;
				queueY[writeOffset] = currentMapY + 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX - 1][currentArrayOffsetY + 1] = 6;
				distances[currentArrayOffsetX - 1][currentArrayOffsetY + 1] = distance;
			}
			if (currentArrayOffsetX < 126 && currentArrayOffsetY < 126 && (directions[currentArrayOffsetX + 1][currentArrayOffsetY + 1]) == 0 && (clipUtils.get(currentClipMapX + 1, currentClipMapY + 2) & 0x12401f8) == 0 && (clipUtils.get(currentClipMapX + 2, currentClipMapY + 2) & NORTH_EAST_MASK) == 0 && (clipUtils.get(currentClipMapX + 2, currentClipMapY + 1) & 0x12401e3) == 0) {
				queueX[writeOffset] = currentMapX + 1;
				queueY[writeOffset] = currentMapY + 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX + 1][currentArrayOffsetY + 1] = 12;
				distances[currentArrayOffsetX + 1][currentArrayOffsetY + 1] = distance;
			}
		}
		foundMapX = currentMapX;
		foundMapY = currentMapY;
		return false;
	}

	static boolean findRouteXAlternative(final int fromMapX, final int fromMapY, final int size, RouteType route, ClipUtils clipUtils) {
		int currentMapX = fromMapX;
		int currentMapY = fromMapY;
		final byte startOffsetX = 64;
		final byte startOffsetY = 64;
		final int baseArrayOffsetX = fromMapX - startOffsetX;
		final int int_6 = fromMapY - startOffsetY;
		directions[startOffsetX][startOffsetY] = 99;
		distances[startOffsetX][startOffsetY] = 0;
		final byte byte_2 = 0;
		int int_7 = 0;
		queueX[byte_2] = fromMapX;
		int int_14 = byte_2 + 1;
		queueY[byte_2] = fromMapY;

		while (true) {
			label313:
			while (true) {
				int currentClipMapX;
				int currentClipMapY;
				int int_10;
				int int_11;
				int int_12;
				int int_13;
				do {
					do {
						do {
							label290:
							do {
								if (int_7 == int_14) {
									foundMapX = currentMapX;
									foundMapY = currentMapY;
									return false;
								}

								currentMapX = queueX[int_7];
								currentMapY = queueY[int_7];
								int_7 = (int_7 + 1) & 0xFFF;
								int_12 = currentMapX - baseArrayOffsetX;
								int_13 = currentMapY - int_6;
								currentClipMapX = currentMapX;
								currentClipMapY = currentMapY;
								if (route.method4274(size, currentMapX, currentMapY, clipUtils)) {
									foundMapX = currentMapX;
									foundMapY = currentMapY;
									return true;
								}

								int_10 = distances[int_12][int_13] + 1;
								if ((int_12 > 0) && (directions[int_12 - 1][int_13] == 0)
									&& ((clipUtils.get(currentClipMapX - 1, currentClipMapY) & 0x124010E) == 0)
									&& ((clipUtils.get(currentClipMapX - 1, (currentClipMapY + size) - 1) & 0x1240138) == 0)) {
									int_11 = 1;

									while (true) {
										if (int_11 >= (size - 1)) {
											queueX[int_14] = currentMapX - 1;
											queueY[int_14] = currentMapY;
											int_14 = (int_14 + 1) & 0xFFF;
											directions[int_12 - 1][int_13] = 2;
											distances[int_12 - 1][int_13] = int_10;
											break;
										}

										if ((clipUtils.get(currentClipMapX - 1, int_11 + currentClipMapY) & 0x124013E) != 0) {
											break;
										}

										++int_11;
									}
								}

								if ((int_12 < (128 - size)) && (directions[int_12 + 1][int_13] == 0)
									&& ((clipUtils.get(currentClipMapX + size, currentClipMapY) & 0x1240183) == 0)
									&& ((clipUtils.get(currentClipMapX + size, (currentClipMapY + size) - 1) & 0x12401E0) == 0)) {
									int_11 = 1;

									while (true) {
										if (int_11 >= (size - 1)) {
											queueX[int_14] = currentMapX + 1;
											queueY[int_14] = currentMapY;
											int_14 = (int_14 + 1) & 0xFFF;
											directions[int_12 + 1][int_13] = 8;
											distances[int_12 + 1][int_13] = int_10;
											break;
										}

										if ((clipUtils.get(currentClipMapX + size, currentClipMapY + int_11) & 0x12401E3) != 0) {
											break;
										}

										++int_11;
									}
								}

								if ((int_13 > 0) && (directions[int_12][int_13 - 1] == 0)
									&& ((clipUtils.get(currentClipMapX, currentClipMapY - 1) & 0x124010E) == 0)
									&& ((clipUtils.get((currentClipMapX + size) - 1, currentClipMapY - 1) & 0x1240183) == 0)) {
									int_11 = 1;

									while (true) {
										if (int_11 >= (size - 1)) {
											queueX[int_14] = currentMapX;
											queueY[int_14] = currentMapY - 1;
											int_14 = (int_14 + 1) & 0xFFF;
											directions[int_12][int_13 - 1] = 1;
											distances[int_12][int_13 - 1] = int_10;
											break;
										}

										if ((clipUtils.get(currentClipMapX + int_11, currentClipMapY - 1) & 0x124018F) != 0) {
											break;
										}

										++int_11;
									}
								}

								if ((int_13 < (128 - size)) && (directions[int_12][int_13 + 1] == 0)
									&& ((clipUtils.get(currentClipMapX, currentClipMapY + size) & 0x1240138) == 0)
									&& ((clipUtils.get((currentClipMapX + size) - 1, currentClipMapY + size) & 0x12401E0) == 0)) {
									int_11 = 1;

									while (true) {
										if (int_11 >= (size - 1)) {
											queueX[int_14] = currentMapX;
											queueY[int_14] = currentMapY + 1;
											int_14 = (int_14 + 1) & 0xFFF;
											directions[int_12][int_13 + 1] = 4;
											distances[int_12][int_13 + 1] = int_10;
											break;
										}

										if ((clipUtils.get(int_11 + currentClipMapX, currentClipMapY + size) & 0x12401F8) != 0) {
											break;
										}

										++int_11;
									}
								}

								if ((int_12 > 0) && (int_13 > 0) && (directions[int_12 - 1][int_13 - 1] == 0)
									&& ((clipUtils.get(currentClipMapX - 1, currentClipMapY - 1) & 0x124010E) == 0)) {
									int_11 = 1;

									while (true) {
										if (int_11 >= size) {
											queueX[int_14] = currentMapX - 1;
											queueY[int_14] = currentMapY - 1;
											int_14 = (int_14 + 1) & 0xFFF;
											directions[int_12 - 1][int_13 - 1] = 3;
											distances[int_12 - 1][int_13 - 1] = int_10;
											break;
										}

										if (((clipUtils.get(currentClipMapX - 1, int_11 + (currentClipMapY - 1)) & 0x124013E) != 0)
											|| ((clipUtils.get(int_11 + (currentClipMapX - 1), currentClipMapY - 1) & 0x124018F) != 0)) {
											break;
										}

										++int_11;
									}
								}

								if ((int_12 < (128 - size)) && (int_13 > 0)
									&& (directions[int_12 + 1][int_13 - 1] == 0)
									&& ((clipUtils.get(currentClipMapX + size, currentClipMapY - 1) & 0x1240183) == 0)) {
									int_11 = 1;

									while (true) {
										if (int_11 >= size) {
											queueX[int_14] = currentMapX + 1;
											queueY[int_14] = currentMapY - 1;
											int_14 = (int_14 + 1) & 0xFFF;
											directions[int_12 + 1][int_13 - 1] = 9;
											distances[int_12 + 1][int_13 - 1] = int_10;
											break;
										}

										if (((clipUtils.get(currentClipMapX + size, int_11 + (currentClipMapY - 1)) & 0x12401E3) != 0)
											|| ((clipUtils.get(currentClipMapX + int_11, currentClipMapY - 1) & 0x124018F) != 0)) {
											break;
										}

										++int_11;
									}
								}

								if ((int_12 > 0) && (int_13 < (128 - size))
									&& (directions[int_12 - 1][int_13 + 1] == 0)
									&& ((clipUtils.get(currentClipMapX - 1, currentClipMapY + size) & 0x1240138) == 0)) {
									for (int_11 = 1; int_11 < size; int_11++) {
										if (((clipUtils.get(currentClipMapX - 1, int_11 + currentClipMapY) & 0x124013E) != 0)
											|| ((clipUtils.get(int_11 + (currentClipMapX - 1), currentClipMapY + size) & 0x12401F8) != 0)) {
											continue label290;
										}
									}

									queueX[int_14] = currentMapX - 1;
									queueY[int_14] = currentMapY + 1;
									int_14 = (int_14 + 1) & 0xFFF;
									directions[int_12 - 1][int_13 + 1] = 6;
									distances[int_12 - 1][int_13 + 1] = int_10;
								}
							}
							while (int_12 >= (128 - size));
						}
						while (int_13 >= (128 - size));
					}
					while (directions[int_12 + 1][int_13 + 1] != 0);
				}
				while ((clipUtils.get(currentClipMapX + size, currentClipMapY + size) & 0x12401E0) != 0);

				for (int_11 = 1; int_11 < size; int_11++) {
					if (((clipUtils.get(int_11 + currentClipMapX, currentClipMapY + size) & 0x12401F8) != 0)
						|| ((clipUtils.get(currentClipMapX + size, currentClipMapY + int_11) & 0x12401E3) != 0)) {
						continue label313;
					}
				}

				queueX[int_14] = currentMapX + 1;
				queueY[int_14] = currentMapY + 1;
				int_14 = (int_14 + 1) & 0xFFF;
				directions[int_12 + 1][int_13 + 1] = 12;
				distances[int_12 + 1][int_13 + 1] = int_10;
			}
		}
	}

	private static boolean findRouteX(int fromMapX, int fromMapY, int size, RouteType route, ClipUtils clipUtils) {
		int currentMapX = fromMapX;
		int currentMapY = fromMapY;
		int currentArrayOffsetX = 64;
		int currentArrayOffsetY = 64;
		int baseArrayOffsetX = fromMapX - currentArrayOffsetX;
		int baseArrayOffsetY = fromMapY - currentArrayOffsetY;
		directions[currentArrayOffsetX][currentArrayOffsetY] = 99;
		distances[currentArrayOffsetX][currentArrayOffsetY] = 0;
		int writeOffset = 0;
		int readOffset = 0;
		queueX[writeOffset] = fromMapX;
		queueY[writeOffset++] = fromMapY;
		w:
		while (readOffset != writeOffset) {
			currentMapX = queueX[readOffset];
			currentMapY = queueY[readOffset];
			readOffset = readOffset + 1 & 0xfff;
			currentArrayOffsetX = currentMapX - baseArrayOffsetX;
			currentArrayOffsetY = currentMapY - baseArrayOffsetY;
			int currentClipMapX = currentMapX;
			int currentClipMapY = currentMapY;
			if (route.method4274(size, currentMapX, currentMapY, clipUtils)) {
				foundMapX = currentMapX;
				foundMapY = currentMapY;
				return true;
			}
			int distance = distances[currentArrayOffsetX][currentArrayOffsetY] + 1;
			if (currentArrayOffsetX > 0 && directions[currentArrayOffsetX - 1][currentArrayOffsetY] == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY) & SOUTH_WEST_MASK) == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY + size - 1) & NORTH_WEST_MASK) == 0) {
				for (int i = 1; i < size; i++) {
					if ((clipUtils.get(currentClipMapX - 1, i + currentClipMapY) & 0x124013e) != 0)
						continue w;
				}
				queueX[writeOffset] = currentMapX - 1;
				queueY[writeOffset] = currentMapY;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX - 1][currentArrayOffsetY] = 2;
				distances[currentArrayOffsetX - 1][currentArrayOffsetY] = distance;
			}
			if (currentArrayOffsetX < 128 - size && directions[currentArrayOffsetX + 1][currentArrayOffsetY] == 0 && (clipUtils.get(currentClipMapX + size, currentClipMapY) & SOUTH_EAST_MASK) == 0 && (clipUtils.get(size + currentClipMapX, size + currentClipMapY - 1) & NORTH_EAST_MASK) == 0) {
				for (int i = 1; i < size; i++) {
					if ((clipUtils.get(size + currentClipMapX, currentClipMapY + i) & 0x12401e3) != 0)
						continue w;
				}
				queueX[writeOffset] = currentMapX + 1;
				queueY[writeOffset] = currentMapY;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX + 1][currentArrayOffsetY] = 8;
				distances[currentArrayOffsetX + 1][currentArrayOffsetY] = distance;
			}
			if (currentArrayOffsetY > 0 && directions[currentArrayOffsetX][currentArrayOffsetY - 1] == 0 && (clipUtils.get(currentClipMapX, currentClipMapY - 1) & SOUTH_WEST_MASK) == 0 && (clipUtils.get(size + currentClipMapX - 1, currentClipMapY - 1) & SOUTH_EAST_MASK) == 0) {
				for (int i = 1; i < size; i++) {
					if ((clipUtils.get(currentClipMapX + i, currentClipMapY - 1) & 0x124018f) != 0)
						continue w;
				}
				queueX[writeOffset] = currentMapX;
				queueY[writeOffset] = currentMapY - 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX][currentArrayOffsetY - 1] = 1;
				distances[currentArrayOffsetX][currentArrayOffsetY - 1] = distance;
			}
			if (currentArrayOffsetY < 128 - size && directions[currentArrayOffsetX][currentArrayOffsetY + 1] == 0 && (clipUtils.get(currentClipMapX, currentClipMapY + size) & NORTH_WEST_MASK) == 0 && (clipUtils.get(currentClipMapX + size - 1, size + currentClipMapY) & NORTH_EAST_MASK) == 0) {
				for (int i = 1; i < size; i++) {
					if ((clipUtils.get(i + currentClipMapX, currentClipMapY + size) & 0x12401f8) != 0)
						continue w;
				}
				queueX[writeOffset] = currentMapX;
				queueY[writeOffset] = currentMapY + 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX][currentArrayOffsetY + 1] = 4;
				distances[currentArrayOffsetX][currentArrayOffsetY + 1] = distance;
			}
			if (currentArrayOffsetX > 0 && currentArrayOffsetY > 0 && directions[currentArrayOffsetX - 1][currentArrayOffsetY - 1] == 0 && (clipUtils.get(currentClipMapX - 1, currentClipMapY - 1) & SOUTH_WEST_MASK) == 0) {
				for (int i = 1; i < size; i++) {
					if ((clipUtils.get(currentClipMapX - 1, i + (currentClipMapY - 1)) & 0x124013e) != 0 || ((clipUtils.get(i + (currentClipMapX - 1), currentClipMapY - 1) & 0x124018f) != 0))
						continue w;
				}
				queueX[writeOffset] = currentMapX - 1;
				queueY[writeOffset] = currentMapY - 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX - 1][currentArrayOffsetY - 1] = 3;
				distances[currentArrayOffsetX - 1][currentArrayOffsetY - 1] = distance;
			}
			if (currentArrayOffsetX < 128 - size && currentArrayOffsetY > 0 && directions[currentArrayOffsetX + 1][currentArrayOffsetY - 1] == 0 && (clipUtils.get(currentClipMapX + size, currentClipMapY - 1) & SOUTH_EAST_MASK) == 0) {
				for (int i = 1; i < size; i++) {
					if ((clipUtils.get(size + currentClipMapX, currentClipMapY - 1 + i) & 0x12401e3) != 0 || (clipUtils.get(i + currentClipMapX, currentClipMapY - 1) & 0x124018f) != 0)
						continue w;
				}
				queueX[writeOffset] = currentMapX + 1;
				queueY[writeOffset] = currentMapY - 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX + 1][currentArrayOffsetY - 1] = 9;
				distances[currentArrayOffsetX + 1][currentArrayOffsetY - 1] = distance;
			}
			if (currentArrayOffsetX > 0 && currentArrayOffsetY < 128 - size && directions[currentArrayOffsetX - 1][currentArrayOffsetY + 1] == 0 && (clipUtils.get(currentClipMapX - 1, size + currentClipMapY) & NORTH_WEST_MASK) == 0) {
				for (int i = 1; i < size; i++) {
					if ((clipUtils.get(currentClipMapX - 1, i + currentClipMapY) & 0x124013e) != 0 || (clipUtils.get(currentClipMapX - 1 + i, size + currentClipMapY) & 0x12401f8) != 0)
						continue w;
				}
				queueX[writeOffset] = currentMapX - 1;
				queueY[writeOffset] = currentMapY + 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX - 1][currentArrayOffsetY + 1] = 6;
				distances[currentArrayOffsetX - 1][currentArrayOffsetY + 1] = distance;
			}
			if (currentArrayOffsetX < 128 - size && currentArrayOffsetY < 128 - size && directions[currentArrayOffsetX + 1][currentArrayOffsetY + 1] == 0 && (clipUtils.get(size + currentClipMapX, size + currentClipMapY) & NORTH_EAST_MASK) == 0) {
				for (int i = 1; i < size; i++) {
					if ((clipUtils.get(currentClipMapX + i, size + currentClipMapY) & 0x12401f8) != 0 || (clipUtils.get(currentClipMapX + size, currentClipMapY + i) & 0x12401e3) != 0)
						continue w;
				}
				queueX[writeOffset] = currentMapX + 1;
				queueY[writeOffset] = currentMapY + 1;
				writeOffset = writeOffset + 1 & 0xfff;
				directions[currentArrayOffsetX + 1][currentArrayOffsetY + 1] = 12;
				distances[currentArrayOffsetX + 1][currentArrayOffsetY + 1] = distance;
			}
		}
		foundMapX = currentMapX;
		foundMapY = currentMapY;
		return false;
	}
}

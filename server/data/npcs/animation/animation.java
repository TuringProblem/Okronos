package io.ruin.data.impl.npcs;

import com.google.gson.annotations.Expose;
import io.ruin.api.utils.JsonUtils;
import io.ruin.cache.NPCDef;
import io.ruin.data.DataFile;

import java.util.List;

public class animation extends DataFile {
    @Override
    public String path() {
        return "npcs/animation/*.json";
    }

    @Override
    public int priority() {
        return 5;
    }

    @Override
    public Object fromJson(String fileName, String json) {
        List<NpcAnimation> list = JsonUtils.fromJson(json, List.class, NpcAnimation.class);

        for(NpcAnimation anim : list){
            NPCDef.get(anim.id).animations = anim;
        }

        return list;
    }

    public static final class NpcAnimation{
        @Expose
        public int id;

        @Expose
        public int spawn_animation, attack_animation, defend_animation, death_animation;
    }
}

# Load environment variables from file
# ai code ps1
Get-Content docker-compose.env | ForEach-Object {
    $_ = $_.Trim()
        if (-not [string]::IsNullOrWhiteSpace($_) -and $_ -match '^[^#][^=]*=.+$') {
            $key, $val = $_ -split '=', 2
            $key = $key.Trim()
            $val = $val.Trim()
            if ($key -and $val) {
                [System.Environment]::SetEnvironmentVariable($key, $val, "Process")
            }
        }
}
# Build Docker image
docker build --file docker\db_sql\Dockerfile . -t kronos/db_sql

# Pipe stack config into docker compose
docker stack config --compose-file docker-compose.yml,docker-compose-local.yml | docker compose -f - up -d

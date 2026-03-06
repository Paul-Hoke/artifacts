$apiKey = $env:GOOGLE_API_KEY
$url = "https://generativelanguage.googleapis.com/v1beta/models/imagen-4.0-generate-001:predict?key=$apiKey"
$logFile = "generate_sprites.log"
$skins = @(
    "forest_2", "forest_1", "forest_goblin_wolfrider2", "mountain_12", "mountain_13", "mountain_14", 
    "forest_goblin_wolfrider1", "forest_lake1", "forest_lake2", "forest_event_lizard1", "forest_4", 
    "forest_goblin1", "mountain_11", "mountain_10", "mountain_9", "forest_goblin2", "forest_orc2", 
    "forest_ogre1", "forest_pig1", "forest_woodcuttingstation1", "mountain_8", "mountain_7", 
    "forest_road24", "forest_cyclops2", "forest_cyclops1", "forest_blueslime1", "forest_yellowslime1", 
    "forest_redslime2", "forest_greenslime1", "forest_orc1", "forest_ogre2", "forest_greenslime2", 
    "forest_redslime1", "forest_blueslime2", "forest_kingslime1", "forest_yellowslime2", "forest_road12", 
    "forest_road13", "forest_road14", "forest_road15", "forest_wolf2", "forest_ashtree2", "spawn", 
    "forest_copperore1", "forest_road11", "forest_wolf1", "forest_chicken1", "forest_cs1", 
    "forest_wcstation1", "forest_gcstation1", "forest_bank1", "forest_grand_exchange1", "forest_ashtree1", 
    "forest_owlbear1", "forest_cow1", "forest_village3", "forest_village1", "forest_village2", 
    "forest_lake3", "forest_lake4", "forest_jewelrycrafting1", "forest_alchemy1", "forest_tailor1", 
    "forest_mushmush1", "mountain_1", "mountain_2", "forest_village5", "forest_flyingserpent1", 
    "forest_mushmush2", "forest_flyingserpent2", "mountain_3", "mountain_4", "forest_miningstation1", 
    "forest_village6", "forest_birchtree2", "mountain_5", "mountain_6", "forest_birchtree1", 
    "forest_coal1", "forest_sprucetree2", "forest_skeleton1", "forest_skeleton2", "forest_skeleton3", 
    "forest_ironore2", "forest_road1", "forest_skeleton4", "forest_skeleton5", "forest_skeleton6", 
    "forest_highwayman1", "forest_skeleton7", "forest_skeleton8", "forest_skeleton9", "forest_sprucetree1", 
    "forest_road3", "forest_cursed_tree1", "forest_glowstem1", "forest_road7", "forest_taskstrader1", 
    "forest_spider1", "forest_cultis1", "forest_imp1", "forest_mapletree1", "forest_sheep1", 
    "forest_hellhound2", "forest_house1", "forest_cultistacolyte1", "forest_road4_cultistwizard", 
    "forest_road6", "forest_road10", "forest_road9", "forest_archaeologist1", "forest_bank2", 
    "forest_runevendor1", "forest_cultistacolyte2", "forest_imp2", "forest_hellhound1", 
    "forest_mapletree2", "forest_nettle1", "forest_coastline3", "forest_coastline4", "forest_coastline5", 
    "forest_road20", "water", "forest_coastline6", "forest_coastline2", "forest_coastline7", 
    "forest_coastline8", "forest_coastline9", "forest_coastline1", "forest_coastline10", 
    "forest_coastline11", "forest_coastline14", "desertisland_1", "desertisland_2", "desertisland_3", 
    "desertisland_17", "desertisland_4", "desertisland_5", "desertisland_6", "desertisland_7", 
    "desertisland_18", "desertisland_8", "desertisland_9", "desertisland_10", "desertisland_11", 
    "desertisland_19", "desertisland_12", "desertisland_13", "desertisland_14", "desertisland_15", 
    "sea2", "desertisland_16"
)

function Log-Message($msg) {
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    Write-Host "$timestamp : $msg"
}

Log-Message "Starting sprite generation for $($skins.Count) skins..."

foreach ($skin in $skins) {
    $filePath = "src/main/resources/static/$skin.png"
    if (Test-Path $filePath) {
        Log-Message "Skipping $skin, already exists."
        continue
    }

    Log-Message "Generating sprite for $skin..."
    $prompt = "A high-quality 2D top-down pixel-art $skin map tile for an MMO. Clear edges for tiling, 64x64 pixel style, vibrant colors."
    
    $body = @{
        instances = @(
            @{ prompt = $prompt }
        )
        parameters = @{
            sampleCount = 1
        }
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri $url -Method Post -Body $body -ContentType "application/json"
        if ($response.predictions -and $response.predictions[0].bytesBase64Encoded) {
            $base64Image = $response.predictions[0].bytesBase64Encoded
            $imageBytes = [Convert]::FromBase64String($base64Image)
            [IO.File]::WriteAllBytes($filePath, $imageBytes)
            Log-Message "Successfully saved ${skin}.png"
        } else {
            Log-Message "Failed to generate ${skin}: No image data in response."
        }
    } catch {
        Log-Message "Error generating ${skin}: $_"
    }
    
    Start-Sleep -Seconds 1
}

Log-Message "Sprite generation complete."


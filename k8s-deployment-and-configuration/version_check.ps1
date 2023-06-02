$postAppVersionUrl = "http://localhost:30001/actuator/info"

while ($true) {
    $response = Invoke-WebRequest -Uri $postAppVersionUrl
    $responseVersion = ($response | ConvertFrom-Json).application.version

    if ($responseVesion -like '1.0.9') {
        Write-Host "POST APP Version: " $responseVersion -ForegroundColor Yellow
    }
    else {
        Write-Host "POST APP Version: 1.0.4" -ForegroundColor Green
    }

    Start-Sleep -Milliseconds 500
}

@echo off
curl -X POST -o %~n0.response.json -d @%~n0.request.json -H "Content-Type: application/json" "http://localhost:8080/repository/%~n0"

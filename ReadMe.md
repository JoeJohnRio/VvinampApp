to run js do
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

./gradlew :composeApp:wasmJsBrowserDistribution         

npx vercel deploy --prod composeApp/build/dist/wasmJs/productionExecutable
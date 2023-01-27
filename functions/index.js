const functions = require("firebase-functions");


exports.helloWorld = functions.https.onRequest((request, response) => {
    functions.logger.info("Hello logs!", {structuredData: true});
    response.send("Hello from Firebase!");
});


exports.addMessage = functions.https.onCall((data, context) => {
    return "testinggg please work";

});

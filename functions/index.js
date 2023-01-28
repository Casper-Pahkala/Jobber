
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
const { getDatabase } = require('firebase-admin/database');

exports.addWelcomeMessages = functions.auth.user().onCreate(async (user) => {
    functions.logger.log('A new user signed in for the first time.');
    // Saves the new welcome message into the database
    // which then displays it in the FriendlyChat clients.
    const db = getDatabase();
    const ref = db.ref('New Users');
    const userRef = db.ref('users/' + user.uid);
    const welcomeRef = ref.child('users');
    const firstName = userRef.val().firstName;
    const lastName = userRef.val().lastName;
    await welcomeRef.set({
        firstName: firstName,
        lastName, lastName,
        timestamp: admin.database.ServerValue.TIMESTAMP,
    });
    functions.logger.log('Welcome message written to database.');
});

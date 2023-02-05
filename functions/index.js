
const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();
const { getDatabase } = require('firebase-admin/database');
const { getMessaging, getToken } = require("firebase-admin/messaging");

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

exports.addDataToDataBase = functions.https.onCall((data, context) => {
    functions.logger.log('A new user signed in for the first time.');
    //User constants
    const userID = data.userID;
    const firstName = data.firstName;
    const lastName = data.lastName;

    const db = getDatabase();
    const userRef = db.ref('users/' + data.uid);

    return userRef.set({

        firstName: firstName,
        lastName, lastName,
        timestamp: admin.database.ServerValue.TIMESTAMP,
    });
});

exports.addNotification = functions.https.onCall((data, context) => {
    functions.logger.log('Reached function');
    
    const payload = {
        notification: {
            title: data.title,
            body: data.body,
        }
    };
    const token = data.token;
    
    

    return admin.messaging().sendToDevice(token, payload);
});

exports.sendChatNotification = functions.https.onCall((data, context) => {
    functions.logger.log('sent chat');

    const payload = {
        notification: {
            title: data.title,
            body: data.body,
        }
    };
    const token = data.token;
    return admin.messaging().sendToDevice(token, payload);
});

exports.sendEmployerNotification = functions.https.onCall((data, context) => {
    functions.logger.log('sent employer noti');

    const payload = {
        notification: {
            title: 'New job available',
            body: data.jobDescription+' within '+data.distance,
        }
    };
    const token = data.token;
    return admin.messaging().sendToDevice(token, payload);
});

exports.sendAcceptedJobNotification = functions.https.onCall((data, context) => {
    functions.logger.log('sent accepted job noti');

    const payload = {
        notification: {
            title: data.jobDescription,
            body: data.name + ' accepted job for '+data.jobDescription,
        }
    };
    const token = data.token;
    return admin.messaging().sendToDevice(token, payload);
});

exports.sendAcceptedWorkerNotification = functions.https.onCall((data, context) => {
    functions.logger.log('sent accepted worker noti');

    const payload = {
        notification: {
            title: data.jobDescription,
            body: data.name + ' accepted you for ' + data.jobDescription,
        }
    };
    const token = data.token;
    return admin.messaging().sendToDevice(token, payload);
});



exports.sendNotification = functions.database.ref('notifications').onUpdate(async (snapshot) => {
    functions.logger.log('updated');
    
    const payload = {
        notification: {
            title: snapshot.child('title').val(),
            body: snapshot.child('body').val(),
        }
    };
    const token = snapshot.child('token').val();
    functions.logger.log('Token: '+ token);
    

    const response = await admin.messaging().sendToDevice(token, payload);
    functions.logger.log('Notifications have been sent');
    await cleanupTokens(response, token);
    functions.logger.log('Notifications tokens cleaned up.');
});






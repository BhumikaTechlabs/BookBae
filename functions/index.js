//const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

let functions = require('firebase-functions');
let admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendPush = functions.database.ref('/Books/{bookId}').onCreate(event => {
	let bookData = event.data.val();
	
	let payload = {
            notification: {
                title: 'BOOK BAE',
                body: 'BOOK BAE just got you "' + bookData.bookName + '" !',
                sound: 'default',
                badge: '1'
            }
        };

        return admin.messaging().sendToTopic('/topics/all', payload);
    
});

/*exports.sendPush2 = functions.database.ref('/Books/{bookId}').onWrite(event => {
    let projectStateChanged = false;
    let projectCreated = false;
    let bookData = event.data.val();
    if (!event.data.previous.exists()) {
        projectCreated = true;
    }
    if (!projectCreated && event.data.changed()) {
        projectStateChanged = true;
    }

    let msg = 'Book details were changed';

		if (projectCreated) {
			msg = `The following new book was added to the library: ${bookData.bookName}`;
		}

    return loadUsers().then(users => {
        let tokens = [];
        for (let user of users) {
            tokens.push(user.pushToken);
        }

        let payload = {
            notification: {
                title: 'Hello There!',
                body: msg,
                sound: 'default',
                badge: '1'
            }
        };

        return admin.messaging().sendToDevice(tokens, payload);
    });
}); */

/*exports.sendWelcomeEmail = functions.auth.user().onCreate(event => {
  // ...
}); */

/*function loadUsers() {
    let dbRef = admin.database().ref('/Users');
    let defer = new Promise((resolve, reject) => {
        dbRef.once('value', (snap) => {
            let data = snap.val();
            let users = [];
            for (var property in data) {
                users.push(data[property]);
            }
            resolve(users);
        }, (err) => {
            reject(err);
        });
    });
    return defer;
} */

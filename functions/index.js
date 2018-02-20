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

/*exports.sendPush = functions.database.ref('/Books/{bookId}').onCreate(event => {
	let bookData = event.data.val();
	
	let payload = {
            notification: {
                title: 'BOOK BAE',
                body: 'BOOK BAE just got you "' + bookData.bookName + '" !',
                sound: 'default',
                badge: '1'
            }
        };

        return admin.messaging().sendToTopic('/topics/newBooks', payload);
    
}); */

//

const nodemailer = require('nodemailer');

const gmailEmail = functions.config().gmail.email;
const gmailPassword = functions.config().gmail.password;
const mailTransport = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: gmailEmail,
    pass: gmailPassword
  }
});

// Your company name to include in the emails
// TODO: Change this to your app or company name to customize the email sent.
const APP_NAME = 'BOOK BAE';


exports.sendWelcomeEmail = functions.auth.user().onCreate(event=> {
    const displayName= event.data.displayName
    const email= event.data.email

    return sendWelcomeEmail(email, displayName);

});

// Sends a welcome email to the given user.
function sendWelcomeEmail(email, displayName) {
  const mailOptions = {
    from: `noreply@bookbae.com`,
    to: email
  };

  // The user subscribed to the newsletter.
  mailOptions.subject = `Welcome to ${APP_NAME}!`;
  mailOptions.text = `Hey ${displayName || ''},\n\nWelcome to ${APP_NAME}! We hope you will enjoy our service. To get started, how about uploading a few books of your own? And yeah, set an attractive price!\n\nLovely to have you here. Let's get started!`;
  return mailTransport.sendMail(mailOptions).then(() => {
    console.log('New welcome email sent to:', email);
  });
}

//

exports.sendFeedbackEmail = functions.database.ref('/Feedbacks/{feedbackId}').onCreate(event=> {

    const usr= event.data.val().user;
    const fbk= event.data.val().feedback
    
    return sendFeedbackEmail(usr, fbk);

});

// Sends a welcome email to the given user.
function sendFeedbackEmail(usr, fbk) {
  const mailOptions = {
    from: `noreply@bookbae.com`,
    to: 'tech.bs.98@gmail.com'
  };

  // The user subscribed to the newsletter.
  mailOptions.subject = `A feedback was posted!`;
  mailOptions.text = 'Hey BOOK BAE,\n\nA feedback was posted by '+usr+'.\n\nFEEDBACK:\n'+fbk;
  return mailTransport.sendMail(mailOptions).then(() => {
    console.log('Feedback email sent to:', 'tech.bs.98@gmail.com');
  });
}

exports.sendNewBookEmailToAdmin = functions.database.ref('/Books/{bookId}').onCreate(event=> {

    const wasFound= event.data.val().wasFound;
    const user= event.data.val().contactPerson;
    const bn= event.data.val().bookName;
    
    return sendNewBookEmailToAdmin(wasFound, user, bn);

});

// Sends a welcome email to the given user.
function sendNewBookEmailToAdmin(wasFound, user, bn) {
  const mailOptions = {
    from: `noreply@bookbae.com`,
    to: 'tech.bs.98@gmail.com'
  };

  // The user subscribed to the newsletter.
  mailOptions.subject = `A new book just got added!`;
  mailOptions.text = 'Hey BOOK BAE,\n\nA new book was posted by '+user+'.\n\nBOOK NAME:\n'+bn+'\n\nBook isbn found: '+wasFound;
  return mailTransport.sendMail(mailOptions).then(() => {
    console.log('New book email sent to:', 'tech.bs.98@gmail.com');
  });
}

exports.sendNewUserEmailToAdmin = functions.auth.user().onCreate(event=> {

    const displayName= event.data.displayName
    const email= event.data.email

    return sendNewUserEmailToAdmin(email, displayName);

});

// Sends a welcome email to the given user.
function sendNewUserEmailToAdmin(email, displayName) {
  const mailOptions = {
    from: `noreply@bookbae.com`,
    to: 'tech.bs.98@gmail.com'
  };

  // The user subscribed to the newsletter.
  mailOptions.subject = `You got a new user!`;
  mailOptions.text = 'Hey BOOK BAE,\n\n'+displayName+' just joined BOOK BAE.\nWe sent a welcome email to them on\n\nE-mail id:\n'+email;
  return mailTransport.sendMail(mailOptions).then(() => {
    console.log('New user email sent to:', 'tech.bs.98@gmail.com');
  });
}

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

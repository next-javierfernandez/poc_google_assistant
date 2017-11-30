'use strict';

// Enable actions client library debugging
process.env.DEBUG = 'actions-on-google:*';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp({
  credential: admin.credential.cert({<credentialCert>}),
  databaseURL: "<databaseURL>"
});

const rooms_ref = admin.database().ref('/rooms');

const DialogflowApp = require('actions-on-google').DialogflowApp;

// Intents
const LIGHT_OFF_INTENT = 'action.light.off';
const LIGHT_ON_INTENT = 'action.light.on';

// Parameters
const LOCATION_PARAM = 'house-location';
const NAME_PARAM = 'name';

// Function
exports.dialogflowFirebaseFulfillment = functions.https.onRequest((request, response) => {
    const app = new DialogflowApp({request: request, response: response});
    
    //define actions map
    let actionMap = new Map();
    actionMap.set(LIGHT_OFF_INTENT, lightOffIntent);
    actionMap.set(LIGHT_ON_INTENT, lightOnIntent);
    app.handleRequest(actionMap);
    
    //Light OFF
    function lightOffIntent(app) {
        const location = app.getArgument(LOCATION_PARAM);
        const name = app.getArgument(NAME_PARAM);
        console.log(`${LIGHT_OFF_INTENT} name:${name} location:${location}`);
        if (name === null && location === null) {
            app.ask(`<speak>No te he entendido nada la verdad</speak>`);
        } else if (location === null) {
            lightOffHome();
        } else {
            turnOffLightsOf(location);
        }
    }
    
    function turnOffLightsOf(location) {
        const location_ref = rooms_ref.child(`${location}`);
        location_ref.once('value', (snapshot) => {
            if (snapshot.val() !== null) {
                const devices_ref = location_ref.child(`devices`);
                devices_ref.once('value', (snapshot) => {
                    var res = `<speak>Vale <break time="1"/> ya las tienes apagadas.</speak>`;
                    snapshot.forEach(function(childSnapshot) {
                        var key = childSnapshot.key;
                        devices_ref.child(key).update(({ status: 'off' }));
                    });
                    app.ask(res);
                });
            } else {
                app.ask(`<speak>No tienes tienes registrado ninguna habitación con el nombre ${location}</speak>`);
            }
        });
    }
    
    function lightOffHome() {
        app.ask(`<speak>Claro, apagando todo</speak>`);
    }
    
    //Light ON
    function lightOnIntent(app) {
        const location = app.getArgument(LOCATION_PARAM);
        const name = app.getArgument(NAME_PARAM);
        console.log(`${LIGHT_ON_INTENT} name:${name} location:${location}`);
        if (name === null && location === null) {
            turnOnAllHome();
        } else if (location === null) {
            turnOnAllHome();
        } else {
            turnOnLightsOf(location);
        }
    }
    
    function turnOnAllHome() {
        app.ask(`<speak>Vale, enciendo toda la casa</speak>`);
    }
    
    function turnOnLightsOf(location) {
        const location_ref = rooms_ref.child(`${location}`);
        location_ref.once('value', (snapshot) => {
            if (snapshot.val() !== null) {
                const devices_ref = location_ref.child(`devices`);
                devices_ref.once('value', (snapshot) => {
                    var res = `<speak>Ok, luces encendidas.</speak>`;
                    snapshot.forEach(function(childSnapshot) {
                        var key = childSnapshot.key;
                        devices_ref.child(key).update(({ status: 'on' }));
                    });
                    app.ask(res);
                });
            } else {
                app.ask(`<speak>No tienes tienes registrado ninguna habitación con el nombre ${location}</speak>`);
            }
        });
    }
  
});
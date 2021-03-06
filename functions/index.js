const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
const root = admin.database().ref();
const db = admin.firestore()

exports.modificarReserva = functions.firestore
    .document('Reservas/{reservaID}')
    .onUpdate((change, context) => {
      const document = change.after.exists ? change.after.data() : null;
      const document2 = change.before.exists ? change.before.data() : null;
      console.log("original" + document.idcliente)
      console.log("nuevo" + document2.idcliente)
      if(document !== null && !(document.idcliente === document2.idcliente)){
          var titulo = document.v.marca;
          var fecha = new Date(parseInt(document.timeInMillis));
          var sfecha = fecha.toLocaleDateString('default');
          console.log(sfecha)
          var payload = null;
          if(document.reservado){
            payload = {
               notification: {
                  title: "Nueva Reserva!",
                  body: "Se ha efectuado una reserva para su vehiculo " +titulo+" en fecha: "+sfecha+"."
               }
             };
          }else{
            payload = {
               notification: {
                  title: "Reserva Cancelada!",
                  body: "Se ha cancelado la reserva para su vehiculo " +titulo+" en fecha: "+sfecha+"."
               }
             };
          }
          var userRef = db.collection('Tokens').doc(document.idofertor);
          return userRef
              .get()
              .then(doc => {
                if (!doc.exists) {
                  throw new Error('No such User document!');
                } else {
                  console.log("enviado")
                  admin.messaging().sendToDevice(doc.data().tok, payload)
                  return true
                }
              })
              .catch(err => {
                console.log(err)
                return false;
              });
          }
          return false;
    });


exports.cancelarEvento = functions.firestore
    .document('Reservas/{reservaID}')
    .onDelete((change, context) => {
      const document = change.exists ? change.data() : null;
      if(document !== null){
          var titulo = document.v.marca;
          var fecha = new Date(parseInt(document.timeInMillis));
          var sfecha = fecha.toLocaleDateString('default');
          console.log(sfecha)
          var payload = null;
          if(document.reservado){
              var userRef = db.collection('Tokens').doc(document.idcliente);
              return userRef
                  .get()
                  .then(doc => {
                    if (!doc.exists) {
                      throw new Error('No such User document!');
                    } else {
                      console.log("enviado")
                       payload = {
                         notification: {
                            title: "Reserva Cancelada!",
                            body: "Se ha cancelado la reserva para el vehiculo " +titulo+" en fecha: "+sfecha+"."
                         }
                       };
                      admin.messaging().sendToDevice(doc.data().tok, payload)
                      return true
                    }
                  })
                  .catch(err => {
                    console.log(err)
                    return false;
                  });
          }
      }
      return false;
    });


exports.mensajeChat = functions.database.ref('/Chat/{cID}/{msgID}').onWrite( async (change, context)  => {
           var t = 'Nuevo mensaje de '+ change.after.val().senderName + '!'
           var b = change.after.val().message
           console.log(t);
           console.log(b);
           var payload = {
               notification: {
                  title: t,
                  body: b
               }
             };
           var ids = context.params.cID.split("_");
           console.log(ids);
           var userRef;
           if (change.after.val().senderUid === ids[0]) {
                userRef = db.collection('Tokens').doc(ids[1]);
           }else{
                userRef = db.collection('Tokens').doc(ids[0]);
           }
           return userRef
               .get()
               .then(doc => {
                 if (!doc.exists) {
                   throw new Error('No such User document!');
                 } else {
                   console.log("enviado")
                   admin.messaging().sendToDevice(doc.data().tok, payload)
                   return true
                 }
               })
               .catch(err => {
                 console.log(err)
                 return false;
               });
});

const nodemailer = require("nodemailer");

const transporter = nodemailer.createTransport({
  service: "gmail",
  auth: {
    user: "kirillshermadov.cs@gmail.com",
    pass: "czvf ujye zeso yjoi",
  },
});

module.exports = transporter;

// async..await is not allowed in global scope, must use a wrapper
async function mailer() {
  // send mail with defined transport object
  const info = await transporter.sendMail({
    from: "kirillshermadov.cs@gmail.com", // sender address
    to: "bar@example.com, baz@example.com", // list of receivers
    subject: "Hello ✔", // Subject line
    text: "Hello world?", // plain text body
    html: "<b>Hello world?</b>", // html body
  });

  console.log("Message sent: %s", info.messageId);
  // Message sent: <d786aa62-4e0a-070a-47ed-0b0666549519@ethereal.email>
}

mailer().catch(console.error);

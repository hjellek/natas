import fetch from 'node-fetch';

let natasURI = `http://natas28:JWwR438wkgTsNKBbcJoowyysdM82YjeF@natas28.natas.labs.overthewire.org`;

async function search (search) {

    let value = eval(search);
    console.log('Searching for', search, value);
    const params = new URLSearchParams();
    params.append('query', value);
    const response = await fetch(natasURI, { method: 'POST', body: params });

    let encodedUri = response.url.replace(`${natasURI}/search.php/?query=`, "");
    let decodedUri = decodeURIComponent(encodedUri);
    let base64data = Buffer.from(decodedUri, 'base64').toString('hex');
    return base64data;
}

async function query (query) {
    query = query.toUpperCase();
    console.log('query', query);
    const base64Encoded = Buffer.from(query, 'hex').toString('base64');
    console.log('base64 encoded', base64Encoded)
    const params = new URLSearchParams();
    params.append('query', base64Encoded);

    let url = `${natasURI}/search.php?${params}`;
    console.log('running query url:', url);
    const response = await fetch(url);
    return response.text();
}

async function start (args) {
    if (args[0] === '-q') {
        const response = await query(args[1]);
        console.log("query response", response);
    } else if (args[0] === '-s') {
        const response = await search(args[1]);
        console.log("search response");
        response.split(/(.{32})/)
            .filter(x => !!x)
            .forEach((part) => {
            console.log(part);
        })
    }
}

start(process.argv.slice(2));



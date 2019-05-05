import request from 'superagent';

const ip = 'http://127.0.0.1:9000/';

const networkLogging = true;

export async function post(extension, data) {
    if (networkLogging) console.log("Attempting to POST to " + extension);

    return await request.post(ip + extension)
        .send(data)
        .then(res => {
            if (res.body != null) {
                return res.body;
            } else {
                return res;
            }
        }).catch(err => {
            if (networkLogging) console.log(JSON.stringify(err));
            return null;
        });
}

export async function get(extension) {
    if (networkLogging) console.log("Attempting to GET from " + extension);

    return await request.get(ip + extension).then(res => {
        if (networkLogging) {console.log("Response: "); console.log(res);}
        if (res.text != null) {
            return res.text;
        } else {
            return res;
        }
    }).catch(err => {
        if (networkLogging) console.log(JSON.stringify(err));
        return null;
    });
}
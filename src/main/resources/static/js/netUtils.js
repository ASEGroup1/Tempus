function getSchedule(){
    return superagent.get("http://localhost:9000/get-generated-schedule").withCredentials().then( res => {
       return res.body;
    }).catch(err => {
        return err;
    })
}
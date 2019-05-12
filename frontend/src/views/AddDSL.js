import React from "react";
import * as NetLib from '../lib/NetworkLib.js';
import Utils from "./DSL/DSLUtils"
import {DSLCreation} from "./DSL/DSLCreation";

export class AddDSL extends React.Component{

    constructor(props){
        super(props);
        this.state = {loadedArgs: false}
    }

    componentDidMount() {
        NetLib.get("dsl/references").then(res => JSON.parse(res)).then(res =>
        {
            Utils.validArgs = res;
            this.setState({loadedArgs: true})
        });
    }

    render() {
        return(
            Utils.validArgs.length ? <DSLCreation/> : <p>Loading</p>
        )
    }
}




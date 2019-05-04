import React from "react";
import {Constraint} from "./Constraint";

export class BranchConstraint extends React.Component{

    constructor(props){
        super(props);
        this.state = {
            validated: false,
        };
        this.callback = props["callback"];
        this.name = props["name"];

        this.caseText= "";
        this.doText= "";
    }

    update(){
        this.callback(this.name + " " + "(" + this.caseText + ") {\n\t"+this.doText.trim().replace(/\n/g, "\n\t") + "\n}")
    }

    render(){
        console.log("rendering");
        return (
            <div>
                <div>
                    <p>{this.name}:</p>
                    <Constraint callback = {(text) => {
                        this.caseText = text;
                        this.update();
                    }}/>
                </div>
                <div>
                    <p>Do:</p>
                    <Constraint callback = {(text) => {
                        this.doText = text;
                        this.update();
                    }}/>
                </div>
            </div>
        )
    }
}
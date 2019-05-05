import React from "react";
import Table from "react-bootstrap/Table";
import {BranchConstraint} from "./BranchConstraint";
import {Constraint} from "./Constraint";

export class ConditionalConstraint extends React.Component{

    constructor(props){
        super(props);
        this.callback = props["callback"];

        this.ifText= [];
        this.defaultBranchText= "";

    }

    update(){
        this.callback(this.ifText + " else {\n\t" + this.defaultBranchText.trim().replace("\n", "\n\t") + "\n}");
    }

    render() {
        return <div>
            <Table>
                <tbody>
                <tr>
                    <td>
                        <BranchConstraint name = "If" callback = {(text) =>{this.ifText = text; this.update()}}/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div >
                            <p>Else:</p>
                            <Constraint callback = {(text) =>{this.defaultBranchText = text; this.update()}}/>
                        </div>
                    </td>
                </tr>

                </tbody>
            </Table>
        </div>

    }
}
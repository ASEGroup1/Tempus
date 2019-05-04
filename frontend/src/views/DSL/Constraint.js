import React from "react";
import {OperationConstraint} from "./OperationConstraint";
import {ConditionalConstraint} from "./ConditionalConstraint";
import Utils from "./DSLUtils";

export class Constraint extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            comparison: true,
            text: ""
        };
        this.callback = props["callback"];
    }

    genTypes(formName, label){
        let options = [];
        options.push(<option>Comparison</option>);
        options.push(<option>Condition</option>);
        return Utils.generateComboBox(options, formName, label, (e) => {
            this.setState({comparison: e.target.value === "Comparison"});

            // update parents that constraint is wiped
            this.callback("")
        }, this.state.comparison? "Comparison": "Condition");
    }

    packForm(){
        let body = [];
        if(this.state.comparison){
            body.push(
                <div>
                    {this.genTypes("Types", "Type")}
                    <OperationConstraint callback={(text) =>
                        // when child updates, update parent
                        this.upPropagate(text)}/>
                </div>
            )
        }else{
            body.push(this.genTypes("Types", "Type"));
            body.push(<ConditionalConstraint callback={(text) =>
                // when child updates, update parent
                this.upPropagate(text)}/>)
        }
        return body;
    }

    upPropagate(text){
        this.callback(text)
    }

    render() {
        return <div style={{border: "1px solid #000000"}}>
            {this.packForm()}
        </div>
    }

}
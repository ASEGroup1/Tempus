import React from "react";
import * as NetLib from '../lib/NetworkLib.js';
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Table from "react-bootstrap/Table";
import {Redirect} from "react-router-dom";
import {DSL} from "./DSL";




let validArgs = [];
let numParams=-1;

export class AddDSL extends React.Component{

    constructor(props){
        super(props);
        this.state = {loadedArgs: false}
    }

    componentDidMount() {
        validArgs = NetLib.get("dsl/references").then(res => JSON.parse(res)).then(res =>
        {
            validArgs = res;
            this.setState({loadedArgs: true})
        });
    }

    render() {
        return validArgs.length ? <DSLCreation/> : <p>Loading</p>
    }

}


class DSLCreation extends React.Component{

    constructor(props){
        super(props);
        this.state = {
            hasWhere: false,
            dslText: "",
            redirect: false
        };
        this.name= "name";
        this.whereText= "";
        this.bodyText= "";
        numParams=1;
    }

    update(){
        let text = ("filter " + this.name + "(" + "Param1" + (numParams == 2? ", Param2": "") + ") {\n\t" + this.bodyText.trim().replace(/\n/g, "\n\t") + "\n} " + (
            this.state.hasWhere? "where (" + this.whereText + ")": ""));
        console.log(text);
        this.setState({dslText: text});
    }



    render() {
        return <div>
            {this.state.redirect? <Redirect to="/dsl" />: null}
            <Form>
                <Form.Row>
                    <Form.Group controlId="name">
                        <Form.Label>Name:</Form.Label>
                        <Form.Control onChange={(event) => {this.name = event.target.value; this.update()}} defaultValue="name" />
                    </Form.Group>

                    <Form.Group controlId="params">
                        <Form.Label>Parameter Count:</Form.Label>
                        <Form.Control as="select" onChange={(event) => {numParams = parseInt(event.target.value); this.update()}} defaultValue="1">
                            <option>1</option>
                            <option>2</option>
                        </Form.Control>
                    </Form.Group>

                    <Form.Group controlId="whereCheck">
                        <Form.Check type="checkbox" label="Where" onChange = {(event) => {
                            this.setState({hasWhere: event.target.checked});
                            this.whereText = "";
                            this.update()
                        }
                        }/>
                    </Form.Group>
                </Form.Row>
            </Form>
            <div>
                Body:
                <Constraint callback = {(text) => {
                    this.bodyText = text;
                    this.update()
                }}/>
            </div>
            {this.state.hasWhere === true?
                <div>
                    Where:
                    <Constraint callback = {(text) => {
                        this.whereText = text;
                        this.update()
                    }}/>
                </div> : null
            }

            <Form><Form.Group controlId="dslText">
                <Form.Label>DSL Text:</Form.Label>
                <Form.Control readOnly as="textarea" value={this.state.dslText} rows = {5}/>
            </Form.Group>
            </Form>
            <Button onClick = {() => {
                NetLib.postToServer("dsl/add", {dsl: this.state.dslText}).then(() =>this.setState({redirect: true}))

            }}>Submit </Button>
        </div>
    }
}




class Constraint extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            comparison: true,
            text: ""
        };
        this.callback = props["callback"];
    }

    genTypes(formName, label, selected){
        let options = [];
        options.push(<option>Comparison</option>);
        options.push(<option>Condition</option>);
        return generateComboBox(options, formName, label, (e) => {
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


class OperationConstraint extends React.Component{

    constructor(props) {
        super(props);
        this.state = {
            option: '',
            argType: validArgs["0"]["type"],
            isArg2: true,
            arg2UserInput: false,
            validated: false,
            text: "",
            isTrue: false
        };
        this.callback = props["callback"];

        this.initialCallback();
    }

    initialCallback(){
        let ref = 'Param1.' + validArgs["0"]["reference"]
        let text = ref + " " + this.getValidOperations(this.state.argType)[0][0]  + " " + ref;
        this.callback(text);
    }

    getOperations(){
        return [
            ["==", /.*/],
            ["!=", /.*/],
            [">",  /float|int/],
            [">=", /float|int/],
            ["<",  /float|int/],
            ["<=", /float|int/],
            ["||", /boolean/],
            ["&&", /boolean/],
            ["!", /boolean/]
        ]
    }

    /**
     * Generate a list of possible parameters
     * @param userInput, if user input is valid
     * @param formName, name of the group
     * @param label, to display
     * @param callback, function to be called when the combobox changes
     */
    getParameterOption(res, userInput, formName, label, callback) {
        let options = [];
        options.push(<option>Param1</option>);
        if(numParams === 2){
            options.push(<option>Param2</option>);
        }
        if(userInput){
            options.push(<option>Manuel</option>);
        }
        if(res) {
            options.push(<option>True</option>)
        }

        return generateComboBox(options, formName, label, callback)
    }

    /**
     * Generate a list of valid arguments
     * @param type, to search for
     * @param formName, name of the group
     * @param label, to display
     * @param callback, function to be called when the combobox changes
     */
    getArgumentOption(type, formName, label, callback){
        let options = [];
        let args = [];
        let typeRegex = type? new RegExp(type.match(/float|int/) ? "float|int": type): /.*/;
        for (let key in validArgs){
            if(validArgs[key]["type"].match(typeRegex)){
                args.push(key)
            }
        }
        for(let arg in args){
            let t = validArgs[args[arg]];
            options.push(<option value = {args[arg]}>{t["reference"] + ": " + t["type"]}</option>)
        }

        return generateComboBox(options, formName, label, callback)
    }

    /**
     * Generate a combobox showing the valid operations
     * @param type, parameter type
     * @param formName, name of the group
     * @param label, to display
     * @param callback, function to be called when the combobox changes
     */
    getOperationOption(type, formName, label, callback){
        let options = [];
        let operations = this.getValidOperations(type)
        for (let o in operations){
            if (!type || type.match(operations[o][1])) {
                options.push(<option value={operations[o][0]}>{operations[o][0]}</option>)
            }
        }

        return generateComboBox(options, formName, label, callback)
    }

    getValidOperations(type){
        let validOperations = [];
        let operations = this.getOperations();
        for (let o in operations) {
            if (!type || type.match(operations[o][1])) {
                validOperations.push(operations[o]);
            }
        }
        return validOperations;
    }


    getUserInputField(type, formName, label, callback){

        var regex = '';
        var placeholderText = '';
        switch (type){
            case "boolean":
                let options = [];
                options.push(<option>True</option>);
                options.push(<option>False</option>)
                return generateComboBox(options, formName, label, null);

            case "int":
            case "float":
                regex = "[+-]?([0-9]*[.])?[0-9]+";
                placeholderText = "1.0";
                break;
            case "char":
                regex = ".";
                placeholderText = "c";
                break;
            case "String":
                regex = ".*";
                placeholderText = "Some text";
                break;
        }

        return <Form.Group controlId={formName}>
            <Form.Label>{label}:</Form.Label>
            <Form.Control
                required
                type="text"
                pattern={regex}
                placeholder={placeholderText}
                isValid={this.state.validated}
            />
            <Form.Control.Feedback type="invalid">
                Please specify a valid {type}.
            </Form.Control.Feedback>
        </Form.Group>
    }

    parseForm(form){
        if(form[0].value == "True"){
            return "True";
        }

        let ref1 = form[0].value + "." + validArgs[form[1].value]["reference"];
        if(form[2].value === "!"){
            return "! "+ ref1
        }else {
            let ref2 = "";
            if (form[3].value === "Manuel"){
                switch (this.state.argType){
                    case "String":
                        ref2 = '"' + form[4].value + '"';
                        break;
                    case "char":
                        ref2 = "'" + form[4].value + "'";
                        break;
                    default:
                        ref2 = form[4].value;
                        break;
                }
            }else{
                ref2 = form[3].value + "." + validArgs[form[4].value]["reference"];
            }
            return ref1 + " "  + form[2].value + " " + ref2;
        }
    }

    handleChange(event) {
        const form = event.currentTarget;
        event.preventDefault();
        this.callback(this.parseForm(form))
    }

    renderWorkaround(){
        let res = [];
        res.push(this.getArgumentOption(null, "ConstraintOperation.Argument1", "Argument 1", (event) => {
                // Set type of argument 1
                this.setState({
                    argType: validArgs[event.target.value]["type"],
                    isArg2: event.target.value[0] != "!"})
            }));

        res.push(this.getOperationOption(this.state.argType, "ConstraintOperation.Operation", "Operation", (event) => {
            this.setState({isArg2: event.target.value[0] != "!"})
        }));

        res.push(this.state.isArg2?
            this.getParameterOption(false, true,"ConstraintOperation.Parameter2", "Parameter 2", (event) => {
                // store whether specified value is user input
                this.setState({arg2UserInput: event.target.value === "Manuel"})
            }): null);

        res.push((this.state.isArg2 ? ((this.state.arg2UserInput) ?
            this.getUserInputField(this.state.argType, "ConstraintOperation.Argument2", "Argument 2", null)
            :this.getArgumentOption(this.state.argType, "ConstraintOperation.Argument2", "Argument 2", null)): null)
        );
        return res;
    }

    render() {
        return  <Form
                noValidate
                onChange={e => this.handleChange(e)}>
                <Form.Row>
                    {this.getParameterOption(true, false, "ConstraintOperation.Parameter1", "Parameter 1", (event) => {
                        this.setState({isTrue: event.target.value == "True"})
                    })}
                    {this.state.isTrue? null: this.renderWorkaround()}
                </Form.Row>
            </Form>

    }
}


class ConditionalConstraint extends React.Component{

    constructor(props){
        super(props);
        this.callback = props["callback"];

        this.ifText= [];
        this.defaultBranchText= "";

    }

    update(){
        this.callback(this.ifText + " else {\n\t" + this.defaultBranchText.trim().replace("\n", "\n\t") + "\n}");
    }

    /*addBranchObject(name){
        // get branchIndex
        let i = this.branchTexts.size;
        // init branch text
        this.branchTexts.push("");
        // add new branch
        this.branchObjects.push(<BranchConstraint callback={(text) => {
            // update text
            this.branchTexts[i] = text;
            // propogate changes
            this.update();
        }} name={name}/>);
    }*/

    /*buildTableBody(){
        let tableBody = [];
        // conditional cases
        for (let e in this.branchObjects){
            tableBody.push(<tr>{this.branchObjects[e]}</tr>)
        }
        // default case
        tableBody.push(<tr>
            <Constraint callback = {(text) =>{this.defaultBranchText = text; this.update()}}/>
        </tr>);

        return tableBody;
    }*/

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


class BranchConstraint extends React.Component{

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




// Helper Methods

/**
 * Generate a combobox.
 * @param options, of the combobox
 * @param formName, name of the group
 * @param label, to display
 * @param callback, function to be called when the combobox changes
 */
function generateComboBox(options, formName, label, callback, selected = ""){
    return <Form.Group controlId={formName}>
        <Form.Label>{label}:</Form.Label>
        <Form.Control as="select" onChange={callback} defaultValue={selected}>
            {options}
        </Form.Control>
    </Form.Group>
}
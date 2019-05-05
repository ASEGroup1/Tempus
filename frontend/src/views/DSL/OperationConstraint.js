import React from "react";
import Form from "react-bootstrap/Form";
import Utils from "./DSLUtils";

export class OperationConstraint extends React.Component{

    constructor(props) {
        super(props);
        this.state = {
            option: '',
            argType: Utils.validArgs["0"]["type"],
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
        let ref = 'Param1.' + Utils.validArgs["0"]["reference"];
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
        if(Utils.numParams === 2){
            options.push(<option>Param2</option>);
        }
        if(userInput){
            options.push(<option>Manual</option>);
        }
        if(res) {
            options.push(<option>True</option>)
        }

        return Utils.generateComboBox(options, formName, label, callback)
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
        for (let key in Utils.validArgs){
            if(Utils.validArgs[key]["type"].match(typeRegex)){
                args.push(key)
            }
        }
        for(let arg in args){
            let t = Utils.validArgs[args[arg]];
            options.push(<option value = {args[arg]}>{t["reference"] + ": " + t["type"]}</option>)
        }

        return Utils.generateComboBox(options, formName, label, callback)
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
        let operations = this.getValidOperations(type);
        for (let o in operations){
            if (!type || type.match(operations[o][1])) {
                options.push(<option value={operations[o][0]}>{operations[o][0]}</option>)
            }
        }

        return Utils.generateComboBox(options, formName, label, callback)
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


    getUserInputField(type, formName, label){
        let regex = '';
        let placeholderText = '';
        switch (type){
            case "boolean":
                let options = [];
                options.push(<option>True</option>);
                options.push(<option>False</option>);
                return Utils.generateComboBox(options, formName, label, null);
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

        let ref1 = form[0].value + "." + Utils.validArgs[form[1].value]["reference"];
        if(form[2].value === "!"){
            return "! "+ ref1
        }else {
            let ref2 = "";
            if (form[3].value === "Manual"){
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
                ref2 = form[3].value + "." + Utils.validArgs[form[4].value]["reference"];
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
                argType: Utils.validArgs[event.target.value]["type"],
                isArg2: event.target.value[0] != "!"})
        }));

        res.push(this.getOperationOption(this.state.argType, "ConstraintOperation.Operation", "Operation", (event) => {
            this.setState({isArg2: event.target.value[0] != "!"})
        }));

        res.push(this.state.isArg2?
            this.getParameterOption(false, true,"ConstraintOperation.Parameter2", "Parameter 2", (event) => {
                // store whether specified value is user input
                this.setState({arg2UserInput: event.target.value === "Manual"})
            }): null);

        res.push((this.state.isArg2 ? ((this.state.arg2UserInput) ?
            this.getUserInputField(this.state.argType, "ConstraintOperation.Argument2", "Argument 2")
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

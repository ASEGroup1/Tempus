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
            isTrue: false,
        };
        this.parameter1Value= "Param1";
        this.argument1Value= Utils.validArgs["0"]["reference"];
        this.operationValue= this.getValidOperations(this.state.argType)[0][0];
        this.parameter2Value= "Param1";
        this.argument2Value= Utils.validArgs["0"]["reference"];
        this.manualValue = "";
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
    getParameterOption(res, userInput, formName, label, callback, defaultValue) {
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

        return Utils.generateComboBox(options, formName, label, callback, defaultValue)
    }

    /**
     * Generate a list of valid arguments
     * @param type, to search for
     * @param formName, name of the group
     * @param label, to display
     * @param callback, function to be called when the combobox changes
     */
    getArgumentOption(type, formName, label, callback, defaultValue){
        let options = [];
        let args = this.getValidArguments(type);
        let dv = null;
        for(let arg in args){
            let t = Utils.validArgs[args[arg]];
            if(t["reference"] === defaultValue){
                dv = args[arg];
            }
            options.push(<option value = {args[arg]}>{t["reference"] + ": " + t["type"]}</option>)
        }

        return Utils.generateComboBox(options, formName, label, callback, dv)
    }

    getValidArguments(type){
        let typeRegex = type? new RegExp(type.match(/float|int/) ? "float|int": type): /.*/;
        let args = [];
        for (let key in Utils.validArgs){
            if(Utils.validArgs[key]["type"].match(typeRegex)){
                args.push(key)
            }
        }
        return args;
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

        return Utils.generateComboBox(options, formName, label, callback, this.operationValue)
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
                regex = ("[(true)(false)]");
                this.manualValue = (!this.manualValue || !RegExp(regex).test(this.manualValue))? "true": this.manualValue;
                let options = [];
                options.push(<option>True</option>);
                options.push(<option>False</option>);
                return Utils.generateComboBox(options, formName, label, (event) => {this.manualValue = event.target.value},this.manualValue );
            case "int":
                regex = "[+-]?[0-9]+";
                placeholderText = "0";
                break;
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
        // if manual value is invalid, make it valid
        this.manualValue = (!this.manualValue || !RegExp(regex).test(this.manualValue))? placeholderText: this.manualValue;
        return <Form.Group controlId={formName}>
            <Form.Label>{label}:</Form.Label>
            <Form.Control
                required
                type="text"
                pattern={regex}
                defaultValue={this.manualValue}
                isValid={this.state.validated}
                onChange={(e) => {this.manualValue = e.target.value}}
            />
            <Form.Control.Feedback type="invalid">
                Please specify a valid {type}.
            </Form.Control.Feedback>
        </Form.Group>
    }

    parseForm(form){
        if(this.parameter1Value === "True"){
            return "True";
        }
        let ref1 = this.parameter1Value+"."+this.argument1Value;

        if(this.operationValue === "!"){
            return "!" + ref1;
        }

        let manv = this.manualValue;
        if(!manv || manv === ""){
            switch(this.state.argType){
                case "boolean":
                    manv = "true";
                    break;
                case "int":
                    manv = "0";
                    break;
                case "float":
                    manv = "1.0";
                    break;
                case "char":
                    manv = "c";
                    break;
                case "String":
                    manv = "Some text";
                    break;
            }
        }


        let ref2 = this.parameter2Value === "Manual"? manv : this.parameter2Value+"."+this.argument2Value;

        return ref1+" "+this.operationValue+" "+ ref2;
    }

    handleChange(event) {
        const form = event.currentTarget;
        event.preventDefault();
        this.callback(this.parseForm(form))
    }

    renderWorkaround(){
        let res = [];
        res.push(this.getArgumentOption(null, "ConstraintOperation.Argument1", "Argument 1", (event) => {
            // if type changed change arg2 value
            if(this.state.argType != Utils.validArgs[event.target.value]["type"]){
                this.argument2Value= Utils.validArgs[this.getValidArguments(Utils.validArgs[event.target.value]["type"])[0]]["reference"];
            }

            // Set type of argument 1
            this.setState({
                argType: Utils.validArgs[event.target.value]["type"],
                isArg2: event.target.value[0] != "!"
            });
            this.argument1Value= Utils.validArgs[event.target.value]["reference"];
        }, this.argument1Value));

        res.push(this.getOperationOption(this.state.argType, "ConstraintOperation.Operation", "Operation", (event) => {
            this.setState({isArg2: event.target.value != "!"});
            this.operationValue= event.target.value;
        }));

        res.push(this.state.isArg2?
            this.getParameterOption(false, true,"ConstraintOperation.Parameter2", "Parameter 2", (event) => {
                // store whether specified value is user input
                this.setState({arg2UserInput: event.target.value === "Manual"});
                this.parameter2Value = event.target.value;
            }, this.parameter2Value): null);

        res.push((this.state.isArg2 ? ((this.state.arg2UserInput) ?
            this.getUserInputField(this.state.argType, "ConstraintOperation.Argument2", "Argument 2")
            :this.getArgumentOption(this.state.argType, "ConstraintOperation.Argument2", "Argument 2", (event) => {
                    // store whether specified value is user input
                this.argument2Value= Utils.validArgs[event.target.value]["reference"];
            },this.argument2Value)): null)
        );
        return res;
    }

    render() {
        return  <Form inline
            noValidate
            onChange={e => this.handleChange(e)}>
            <Form.Row>
                {this.getParameterOption(true, false, "ConstraintOperation.Parameter1", "Parameter 1", (event) => {
                    this.setState({isTrue: event.target.value == "True"});
                    this.parameter1Value= event.target.value;
                }, this.parameter1Value)}
                {this.state.isTrue? null: this.renderWorkaround()}
            </Form.Row>
        </Form>

    }
}

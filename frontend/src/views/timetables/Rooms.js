import React from "react";
import Table from "react-bootstrap/Table";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Button from "react-bootstrap/Button";


export class Rooms extends React.Component {

    constructor(props) {
        super(props);
        this.state = {displayData: [
                ["-","-","-","-","-"],
                ["-","-","-","-","-"],
                ["-","-","-","-","-"],
                ["-","-","-","-","-"],
                ["-","-","-","-","-"],
                ["-","-","-","-","-"],
                ["-","-","-","-","-"],
                ["-","-","-","-","-"],
                ["-","-","-","-","-"],
                ["-","-","-","-","-"],
                ["-","-","-","-","-"],
                ["-","-","-","-","-"]
            ]};
    }


    getSchedule(week) {
        if (week === 1) {
            this.setState({displayData: [
                    ["Week 1","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-",""],
                    ["ESRS","-","-","-","-"],
                    ["ESRS","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","Web Apps & Services","-","-"],
                    ["-","-","Web Apps & Services","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else if (week === 2) {
            this.setState({displayData: [
                    ["Week 2","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-",<div><h1>Hi</h1><p>bye</p></div>,"-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else if (week === 3) {
            this.setState({displayData: [
                    ["Week 3","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else if (week === 4) {
            this.setState({displayData: [
                    ["Week 4","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else if (week === 5) {
            this.setState({displayData: [
                    ["Week 5","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else if (week === 6) {
            this.setState({displayData: [
                    ["Week 6","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else if (week === 7) {
            this.setState({displayData: [
                    ["Week 7","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else if (week === 8) {
            this.setState({displayData: [
                    ["Week 8","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else if (week === 9) {
            this.setState({displayData: [
                    ["Week 9","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else if (week === 10) {
            this.setState({displayData: [
                    ["Week 10","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else if (week === 11) {
            this.setState({displayData: [
                    ["Week 11","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else if (week === 12) {
            this.setState({displayData: [
                    ["Week 12","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
        } else {
            this.setState({displayData: [
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"],
                    ["-","-","-","-","-"]
                ]});
            alert("Error");
        }
    }

    render() {

        return (
            <div>
                <p>Timetable - Rooms</p>
                <ButtonGroup>
                    <Button onClick={()=> this.getSchedule(1)}>1</Button>
                    <Button onClick={()=> this.getSchedule(2)}>2</Button>
                    <Button onClick={()=> this.getSchedule(3)}>3</Button>
                    <Button onClick={()=> this.getSchedule(4)}>4</Button>
                    <Button onClick={()=> this.getSchedule(5)}>5</Button>
                    <Button onClick={()=> this.getSchedule(6)}>6</Button>
                    <Button onClick={()=> this.getSchedule(7)}>7</Button>
                    <Button onClick={()=> this.getSchedule(8)}>8</Button>
                    <Button onClick={()=> this.getSchedule(9)}>9</Button>
                    <Button onClick={()=> this.getSchedule(10)}>10</Button>
                    <Button onClick={()=> this.getSchedule(11)}>11</Button>
                    <Button onClick={()=> this.getSchedule(12)}>12</Button>
                </ButtonGroup>
                <p></p>
                <Table striped bordered hover variant="dark">
                    <thead>
                    <tr>
                        <th></th>
                        <th>Monday</th>
                        <th>Tuesday</th>
                        <th>Wednesday</th>
                        <th>Thursday</th>
                        <th>Friday</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>08:00</td>
                        <td>{this.state.displayData[0][0]}</td>
                        <td>{this.state.displayData[0][1]}</td>
                        <td>{this.state.displayData[0][2]}</td>
                        <td>{this.state.displayData[0][3]}</td>
                        <td>{this.state.displayData[0][4]}</td>
                    </tr>
                    <tr>
                        <td>09:00</td>
                        <td>{this.state.displayData[1][0]}</td>
                        <td>{this.state.displayData[1][1]}</td>
                        <td>{this.state.displayData[1][2]}</td>
                        <td>{this.state.displayData[1][3]}</td>
                        <td>{this.state.displayData[1][4]}</td>
                    </tr>
                    <tr>
                        <td>10:00</td>
                        <td>{this.state.displayData[2][0]}</td>
                        <td>{this.state.displayData[2][1]}</td>
                        <td>{this.state.displayData[2][2]}</td>
                        <td>{this.state.displayData[2][3]}</td>
                        <td>{this.state.displayData[2][4]}</td>
                    </tr>
                    <tr>
                        <td>11:00</td>
                        <td>{this.state.displayData[3][0]}</td>
                        <td>{this.state.displayData[3][1]}</td>
                        <td>{this.state.displayData[3][2]}</td>
                        <td>{this.state.displayData[3][3]}</td>
                        <td>{this.state.displayData[3][4]}</td>
                    </tr>
                    <tr>
                        <td>12:00</td>
                        <td>{this.state.displayData[4][0]}</td>
                        <td>{this.state.displayData[4][1]}</td>
                        <td>{this.state.displayData[4][2]}</td>
                        <td>{this.state.displayData[4][3]}</td>
                        <td>{this.state.displayData[4][4]}</td>
                    </tr>
                    <tr>
                        <td>13:00</td>
                        <td>{this.state.displayData[5][0]}</td>
                        <td>{this.state.displayData[5][1]}</td>
                        <td>{this.state.displayData[5][2]}</td>
                        <td>{this.state.displayData[5][3]}</td>
                        <td>{this.state.displayData[5][4]}</td>
                    </tr>
                    <tr>
                        <td>14:00</td>
                        <td>{this.state.displayData[6][0]}</td>
                        <td>{this.state.displayData[6][1]}</td>
                        <td>{this.state.displayData[6][2]}</td>
                        <td>{this.state.displayData[6][3]}</td>
                        <td>{this.state.displayData[6][4]}</td>
                    </tr>
                    <tr>
                        <td>15:00</td>
                        <td>{this.state.displayData[7][0]}</td>
                        <td>{this.state.displayData[7][1]}</td>
                        <td>{this.state.displayData[7][2]}</td>
                        <td>{this.state.displayData[7][3]}</td>
                        <td>{this.state.displayData[7][4]}</td>
                    </tr>
                    <tr>
                        <td>16:00</td>
                        <td>{this.state.displayData[8][0]}</td>
                        <td>{this.state.displayData[8][1]}</td>
                        <td>{this.state.displayData[8][2]}</td>
                        <td>{this.state.displayData[8][3]}</td>
                        <td>{this.state.displayData[8][4]}</td>
                    </tr>
                    <tr>
                        <td>17:00</td>
                        <td>{this.state.displayData[9][0]}</td>
                        <td>{this.state.displayData[9][1]}</td>
                        <td>{this.state.displayData[9][2]}</td>
                        <td>{this.state.displayData[9][3]}</td>
                        <td>{this.state.displayData[9][4]}</td>
                    </tr><tr>
                        <td>18:00</td>
                        <td>{this.state.displayData[10][0]}</td>
                        <td>{this.state.displayData[10][1]}</td>
                        <td>{this.state.displayData[10][2]}</td>
                        <td>{this.state.displayData[10][3]}</td>
                        <td>{this.state.displayData[10][4]}</td>
                    </tr>
                    <tr>
                        <td>19:00</td>
                        <td>{this.state.displayData[11][0]}</td>
                        <td>{this.state.displayData[11][1]}</td>
                        <td>{this.state.displayData[11][2]}</td>
                        <td>{this.state.displayData[11][3]}</td>
                        <td>{this.state.displayData[11][4]}</td>
                    </tr>
                    </tbody>
                </Table>
            </div>
        );
    }
}
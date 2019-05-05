import React from "react";
import Table from "react-bootstrap/Table";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Button from "react-bootstrap/Button";
import {getTimetable} from "../../RequestManager";
import Dropdown from "react-bootstrap/Dropdown";

const weeks = Array.apply(null, {length: 13}).map(Number.call, Number).splice(1);
const DAYS = ["", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
const WEEK_LENGTH = 5;

export class Students extends React.Component {
    constructor(props) {
        super(props);

        this.populateRoomTimetable();
        this.state = {timetable: [], fullTimetable: {}, weekIndex: 1, roomId: "1B2"};
    }

    populateRoomTimetable = async () => {
        await this.setState({fullRoomTimetable: await getTimetable()});
        console.debug(this.state.fullRoomTimetable);
        this.generateSchedule(1);
    };

    generateSchedule(w) {
        this.setState({timetable: this.state.fullRoomTimetable[this.state.roomId].slice(w * WEEK_LENGTH -1, w * WEEK_LENGTH  + WEEK_LENGTH- 1)});
    }

    genTable() {
        let body = [];
        let time = 8;
        let head = [];

        for (let i = 0; i < 6; i++)
            head.push(<th>{DAYS[i]}</th>);

        let fullTable = [(<thead><tr>{head}</tr></thead>)];

        for (let i = 0; i <= 11; i++) {
            let rows = [];
            for (let j = 0; j <= 4; j++)
                rows.push(<td>{this.state.timetable[j][i]}</td>);

            rows.unshift(<td>{++time}:00</td>);
            body.push(<tr>{rows}</tr>);
        }

        fullTable.push(<tbody>{body}</tbody>);

        return fullTable;
    }

    changeRoom(room) {
        this.setState({roomId: room});
        this.generateSchedule(this.state.weekIndex);
    }

    render() {
        return (
            (this.state.timetable.length > 0) ?
                <div>
                    <h1>Timetable for room {this.state.roomId}</h1>

                    <ButtonGroup>
                        {weeks.map(w => (<Button onClick={() => this.generateSchedule(w)}>{w}</Button>))}
                    </ButtonGroup>

                    <Dropdown style={{float:'left', height: '800px', width: '200px',  zIndex: 50, background: 'transparent'}}>
                        <Dropdown.Toggle>Rooms</Dropdown.Toggle>

                        <Dropdown.Menu>
                            {Object.keys(this.state.fullRoomTimetable).map(r => <Dropdown.Item onClick={() => this.changeRoom(r)}>{r}</Dropdown.Item>)}
                        </Dropdown.Menu>
                    </Dropdown>
                    <br/>
                    <Table striped bordered hover variant="dark" style={{position: 'absolute', top: '300px'}}>
                        {this.genTable()}
                    </Table>
                </div> : ""
        );
    }
}
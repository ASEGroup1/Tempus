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
                ],
                weeks: [1,2,3,4,5,6,7,8,9,10,11,12]
        };
    }

    getSchedule(week) {
        switch (week) {
            case 1:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            case 2:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            case 3:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            case 4:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            case 5:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            case 6:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            case 7:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            case 8:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            case 9:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            case 10:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            case 11:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            case 12:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        if (!(i===0 && j===0)) {
                            this.state.displayData[i][j] = this.getCellData();
                        } else {
                            this.state.displayData[i][j] = "Week " + week;
                        }
                    }
                }
                this.setState({displayData: this.state.displayData});
                break;
            default:
                for (let i=0; i<12; i++) {
                    for (let j=0; j<5; j++) {
                        this.state.displayData[i][j] = "-";
                    }
                }
                this.setState({displayData: this.state.displayData});
                alert("Error");
                break;
        }
    }

    getCellData() {
        return "-";
    }

    genTable() {
        let fullTable = [];
        let body = [];
        let time = 8;
        let head = [];
        const days = ["","Monday","Tuesday","Wednesday","Thursday","Friday"];


        for (let i=0; i<6; i++) {
            head.push(<th>{days[i]}</th>);
        }

        fullTable.push(<thead><tr>{head}</tr></thead>);

        for (let i=0; i<12; i++) {
            let rows = [];
            for (let j=0; j<6; j++) {
                if (j!==0) {
                    rows.push(<td>{this.state.displayData[i][j-1]}</td>);
                } else {
                    rows.push(<td>{time}:00</td>);
                    time++;
                }
            }
            body.push(<tr>{rows}</tr>);
        }

        fullTable.push(<tbody>{body}</tbody>);
        return fullTable;
    }

    render() {

        return (
            <div>
                <p>Timetable - Rooms</p>
                <ButtonGroup>
                    {this.state.weeks.map(w => (<Button onClick={() => this.getSchedule(w)}>{w}</Button>))}
                </ButtonGroup>
                <p></p>
                <Table striped bordered hover variant="dark">
                    {this.genTable()}
                </Table>
            </div>
        );
    }
}
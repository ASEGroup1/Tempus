import React from "react";

export class Home extends React.Component {
	render() {
		return <div style={{fontSize: '18px'}}>
			<h1>Tempus - A Customisable Timetabler</h1>
			<h2> What is Tempus? </h2>
			<p style={{marginLeft: '15%', marginRight: '15%'}}>Tempus is a timetabling system that allows for "custom constraints" allowing you to customise
			how our scheduling algorithm generates your timetable, for example you could specify that no sessions could begin before
			10am and our scheduler would attempt to generate that timetable.</p>
			<h2> Limitations </h2>
			<p style={{marginLeft: '15%', marginRight: '15%'}}>Tempus isn't magic, it can't
				schedule something that isn't possible. If you've got 1000 students, 3 tiny rooms and 2 days to schedule all events, not even computer magic can make
				you a working timetable. </p>
			<h2> How to use Tempus </h2>
			<p style={{marginLeft: '15%', marginRight: '15%'}}>Tempus doesn't assume what you want, you have to specify that.
				Meaning if on average only 80% of students attend lectures, you can schedule them in rooms with only 80% capacity, if that's against the rules of your establishment,
				no problem you can specify that too.</p>

		</div>
	}
}
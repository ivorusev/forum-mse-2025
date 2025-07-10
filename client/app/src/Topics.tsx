import { useEffect, useState } from 'react';
import { CalendarCheck, CalendarArrowUp } from 'lucide-react';

type Topic = {
    title: string;
    createdOn: string;
    modifiedOn: string;
}

export function Topics() {
	const [topics, setTopics] = useState<Topic[]>([]);
	useEffect(() => {
		fetch('http://localhost:8080/topics')
			.then(response => response.json())
			.then(data => setTopics(data.content))
			.catch(error => console.error('Error fetching topics:', error));
	}, []);

	return (
		<div>
			<h2>All Topics</h2>
			<ul>
				{topics.map(topic => (
					<li key={topic.title}>
						<h2>{topic.title}</h2>
						<p><CalendarCheck className="d-inline-block w-4 h-4 text-gray-400" /> created: {new Date(topic.createdOn).toLocaleDateString("bg-BG")}</p>
						<p><CalendarArrowUp className="d-inline-block w-4 h-4 text-gray-400" /> modified: {new Date(topic.modifiedOn).toLocaleDateString("bg-BG")}</p>
					</li>
				))}
			</ul>
		</div>
	);
}

export default Topics;


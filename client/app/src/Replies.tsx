import { useEffect, useState } from 'react';
import Topics from './Topics';

type Topic = {
    title: string;
    createdOn: string;
    modifiedOn: string;
}

export function Replies() {
	const [topics, setTopics] = useState<Topic[]>([]);
	useEffect(() => {
		fetch('http://localhost:8080/replies/id')
			.then(response => response.json())
			.then(data => setTopics(data.content))
			.catch(error => console.error('Error fetching replies:', error));
	}, []);

	return (
		<div>
			<h2>Topics</h2>
			<ul>
				{topics.map(topic => (
					<li key={topic.title}>
						<h2>{topic.title}</h2>
						<p>{new Date(topic.createdOn).toLocaleDateString("bg-BG")}</p>
						<p>{topic.modifiedOn}</p>
					</li>
				))}
			</ul>
		</div>
	);
}

export default Topics;


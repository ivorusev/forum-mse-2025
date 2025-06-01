import { useEffect, useState } from 'react';

type Topic = {
    title: string;
    createdOn: Date;
    modifiedOn: Date;
}

export function Topics() {

        function dateToString(date:Date)
        {
            return  (date.getDay() +1)+ "." + date.getMonth() + "." + date.getFullYear() + " | " +  date.getHours() + ":" + date.getMinutes();
        }

        const [topics, setTopics] = useState<Topic[]>([]);
        useEffect( () => {
             fetch('http://localhost:8080/topics')
                .then(response => response.json())
                .then(data => {
                                    data = data.content.map((t:Topic) => { return {...t, "createdOn":new Date(t.createdOn),  "modifiedOn":new Date(t.modifiedOn)} })
                                    setTopics(data)
                                }
                                )
               .catch(error => console.error('Error fetching topics:', error));

        }, []);

        return (
            <div>
                <h2>Topics</h2>
                <ul>
                    {topics.map(topic => (
                        <li key={topic.title}>
                            <h2>{topic.title}</h2>
                            <p>{ dateToString(topic.createdOn) }</p>
                            <p>{ dateToString(topic.modifiedOn) }</p>
                            </li>
                    ))}
                </ul>
            </div>

        );
}

export default Topics;


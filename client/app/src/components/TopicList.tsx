import React, { useEffect, useState } from 'react';

type Topic = {
    id: number;
    title: string;
    content?: string;
    createdOn: Date;
    modifiedOn: Date;
};

const TopicList: React.FC = () => {
    const [topics, setTopics] = useState<Topic[]>([]);

    const fetchTopics = async () => {
        const res = await fetch('/topics');
        const data = await res.json();
        setTopics(data.content);
    };


    useEffect(() => {
        fetchTopics();
    }, []);

    return (
        <div>
            <h2>Теми</h2>
            <ul>
                {topics.map(topic => (
                    <li key={topic.id}>
                        {topic.title}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default TopicList;

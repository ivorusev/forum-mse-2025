import React, {useEffect, useState} from 'react';
import type {ReplyResponse, TopicResponse, UserResponse} from "../types.ts";
import '../AdminPanel.css';

type View = 'users' | 'topics' | 'replies';

const AdminPanel: React.FC = () => {
    const [view, setView] = useState<View>('users');
    const [data, setData] = useState<UserResponse[] | TopicResponse[] | ReplyResponse[]>([]);

    useEffect(() => {
        loadData('users');
    }, []);

    const loadData = async (type: View, topicId?: number) => {
        setView(type);
        let url = '';
        if (type === 'users') url = 'http://localhost:8080/users?page=0&size=20';
        if (type === 'topics') url = 'http://localhost:8080/topics?page=0&size=20';
        if (type === 'replies') url = `http://localhost:8080/replies/topic/${topicId}?page=0&size=20`;

        try {
            const res = await fetch(url);
            const result = await res.json();
            setData(result.content ?? result);
        } catch (error) {
            console.error(`Failed to load ${type}:`, error);
        }
    };

    const renderTable = () => {
        if (view === 'users') {
            const users = data as UserResponse[];
            return (
                <div className="table-wrapper">
                    <table className="table table-striped mt-3">
                        <thead>
                        <tr>
                            <th>Потребителско име</th>
                            <th>Имейл</th>
                            <th>Действия</th>
                        </tr>
                        </thead>
                        <tbody>
                        {users.map((user) => (
                            <tr key={user.id}>
                                <td data-label="Потребителско име">{user.username}</td>
                                <td data-label="Имейл">{user.email}</td>
                                <td data-label="Действия">
                                    <button
                                        className="btn btn-sm btn-danger"
                                        onClick={() => deleteUser(user.id)}
                                    >
                                        Изтрий
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            );
        }

        if (view === 'topics') {
            const topics = data as TopicResponse[];
            return (
                <div className="table-wrapper">
                    <table className="table table-striped mt-3">
                        <thead>
                        <tr>
                            <th>Заглавие</th>
                            <th>Създадено от</th>
                            <th>Създадено на</th>
                            <th>Действия</th>
                        </tr>
                        </thead>
                        <tbody>
                        {topics.map((topic) => (
                            <tr key={topic.id}>
                                <td data-label="Заглавие">{topic.title}</td>
                                <td data-label="Създадено от">{topic.authorUsername}</td>
                                <td data-label="Създадено на">{new Date(topic.createdOn).toLocaleString()}</td>
                                <td data-label="Действия">
                                    <div className="d-flex gap-2">
                                        <button
                                            className="btn btn-sm btn-info"
                                            onClick={() => loadData('replies', topic.id)}
                                        >
                                            Коментари
                                        </button>
                                        <button
                                            className="btn btn-sm btn-danger"
                                            onClick={() => deleteTopic(topic.title)}
                                        >
                                            Изтрий
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            );
        }

        if (view === 'replies') {
            const replies = data as ReplyResponse[];
            return (
                <div>
                    <button className="btn btn-secondary my-3" onClick={() => loadData('topics')}>
                        Назад към темите
                    </button>
                    <div className="table-wrapper"> {}
                        <table className="table table-striped mt-3">
                            <thead>
                            <tr>
                                <th>Заглавие на Темата</th>
                                <th>Съдържание</th>
                                <th>Автор</th>
                                <th>Създадено на</th>
                                <th>Действия</th>
                            </tr>
                            </thead>
                            <tbody>
                            {replies.map((reply) => (
                                <tr key={reply.id}>
                                    <td data-label="Заглавие на Темата">{reply.topicTitle}</td>
                                    <td data-label="Съдържание">{reply.replyBody}</td>
                                    <td data-label="Автор">{reply.authorUsername}</td>
                                    <td data-label="Създадено на">{new Date(reply.createdOn).toLocaleString()}</td>
                                    <td data-label="Действия">
                                        <button
                                            className="btn btn-sm btn-danger"
                                            onClick={() => deleteReply(reply.id, reply.id)}
                                        >
                                            Изтрий
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            );
        }

        return null;
    };


    const deleteUser = async (id: number) => {
        if (!window.confirm("Сигурен ли си, че искаш да изтриеш този потребител?")) return;
        try {
            await fetch(`http://localhost:8080/users/${id}`, {method: 'DELETE'});
            await loadData('users');
        } catch (error) {
            console.error("Failed to delete user:", error);
            alert("Неуспешно изтриване на потребителя.");
        }
    };

    const deleteTopic = async (title: string) => {
        if (!window.confirm("Сигурен ли си, че искаш да изтриеш тази тема?")) return;
        try {
            await fetch(`http://localhost:8080/topics/${title}`, {method: 'DELETE'});
            await loadData('topics');
        } catch (error) {
            console.error("Failed to delete topic:", error);
            alert("Неуспешно изтриване на темата.");
        }
    };

    const deleteReply = async (id: number, topicId: number) => {
        if (!window.confirm("Сигурен ли си, че искаш да изтриеш този коментар?")) return;
        try {
            await fetch(`http://localhost:8080/replies/${id}`, {method: 'DELETE'});
            await loadData('replies', topicId);
        } catch (error) {
            console.error("Failed to delete reply:", error);
            alert("Неуспешно изтриване на коментара.");
        }
    };

    return (
        <div className="admin-panel-container">
            <h2>Административен панел</h2>
            <div className="btn-group mt-3">
                <button className="btn btn-outline-primary" onClick={() => loadData('users')}>
                    Потребители
                </button>
                <button className="btn btn-outline-success" onClick={() => loadData('topics')}>
                    Теми
                </button>
            </div>
            <div className="table-wrapper mt-4">
                {renderTable()}
            </div>
        </div>
    );
};

export default AdminPanel;
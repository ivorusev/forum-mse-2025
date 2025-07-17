import React, {useEffect, useState} from 'react';
import type {ReplyResponse} from '../types.ts';

type ReplyListProps = {
    topicId: number;
};

const ReplyList: React.FC<ReplyListProps> = ({topicId}) => {
    const [replies, setReplies] = useState<ReplyResponse[]>([]);

    const fetchReplies = async () => {
        try {
            const res = await fetch(`/replies/topic/${topicId}`);
            const data = await res.json();
            setReplies(data.content);
        } catch (error) {
            console.error("Грешка при зареждане на отговорите:", error);
        }
    };

    const deleteReply = async (replyId: number) => {
        try {
            await fetch(`/replies/${replyId}`, {
                method: 'DELETE',
            });
            fetchReplies();
        } catch (error) {
            console.error("Грешка при изтриване на отговор:", error);
        }
    };

    useEffect(() => {
        fetchReplies();
    }, [topicId]);

    return (
        <div>
            <h3>Отговори по темата</h3>
            {replies.length === 0 ? (
                <p>Няма отговори.</p>
            ) : (
                <ul>
                    {replies.map(reply => (
                        <li key={reply.id}>
                            <p><strong>{reply.createdOn}</strong>: {reply.replyBody}</p>
                            <button onClick={() => deleteReply(reply.id)}>Изтрий</button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default ReplyList;

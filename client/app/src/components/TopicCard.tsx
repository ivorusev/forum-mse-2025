import React from 'react';
import type { Topic } from '../types/forum';
import { VoteButtons } from './VoteButtons';
import './TopicCard.css';

interface TopicCardProps {
    topic: Topic;
    onVote: (topicId: number, voteType: 'upvote' | 'downvote') => void;
    onClick?: (topicId: number) => void;
}

export const TopicCard: React.FC<TopicCardProps> = ({ topic, onVote, onClick }) => {
    const formatDate = (date: Date) => {
        const now = new Date();
        const diffInHours = Math.floor((now.getTime() - date.getTime()) / (1000 * 60 * 60));
        
        if (diffInHours < 1) {
            const diffInMinutes = Math.floor((now.getTime() - date.getTime()) / (1000 * 60));
            return `${diffInMinutes}m ago`;
        } else if (diffInHours < 24) {
            return `${diffInHours}h ago`;
        } else {
            const diffInDays = Math.floor(diffInHours / 24);
            return `${diffInDays}d ago`;
        }
    };

    const handleCardClick = () => {
        if (onClick) {
            onClick(topic.id);
        }
    };

    return (
        <div className="topic-card">
            <div className="topic-vote-section">
                <VoteButtons
                    itemId={topic.id}
                    voteCount={topic.voteCount ?? 0}
                    userVote={topic.userVote || null}
                    onVote={onVote}
                />
            </div>
            
            <div className="topic-content" onClick={handleCardClick}>
                <div className="topic-header">
                    <h3 className="topic-title">{topic.title}</h3>
                </div>
                
                <div className="topic-meta">
                    <span className="topic-time">
                        Posted {formatDate(topic.createdOn)}
                    </span>
                    {topic.modifiedOn.getTime() !== topic.createdOn.getTime() && (
                        <span className="topic-edited">
                            • Edited {formatDate(topic.modifiedOn)}
                        </span>
                    )}
                </div>
            </div>
        </div>
    );
};

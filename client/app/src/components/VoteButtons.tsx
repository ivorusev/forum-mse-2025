import React from 'react';
import type { VoteType } from '../types/forum';
import './VoteButtons.css';

interface VoteButtonsProps {
    itemId: number;
    voteCount: number;
    userVote: VoteType | null;
    onVote: (itemId: number, voteType: VoteType) => void;
}

export const VoteButtons: React.FC<VoteButtonsProps> = ({
    itemId,
    voteCount,
    userVote,
    onVote
}) => {
    const handleUpvote = () => {
        onVote(itemId, 'upvote');
    };

    const handleDownvote = () => {
        onVote(itemId, 'downvote');
    };

    return (
        <div className="vote-buttons">
            <button
                className={`vote-btn upvote ${userVote === 'upvote' ? 'active' : ''}`}
                onClick={handleUpvote}
                aria-label="Upvote"
            >
                <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
                    <path d="M8 2L14 8H10V14H6V8H2L8 2Z"/>
                </svg>
            </button>
            
            <span className={`vote-count ${voteCount > 0 ? 'positive' : voteCount < 0 ? 'negative' : ''}`}>
                {voteCount}
            </span>
            
            <button
                className={`vote-btn downvote ${userVote === 'downvote' ? 'active' : ''}`}
                onClick={handleDownvote}
                aria-label="Downvote"
            >
                <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
                    <path d="M8 14L2 8H6V2H10V8H14L8 14Z"/>
                </svg>
            </button>
        </div>
    );
};

import type { Reply, VoteType } from '../types/forum';
import { VoteButtons } from './VoteButtons';
import './ReplyCard.css';

interface ReplyCardProps {
    reply: Reply;
    onVote: (itemId: number, voteType: VoteType) => void;
}

export function ReplyCard({ reply, onVote }: ReplyCardProps) {
    return (
        <div className="reply-card">
            <div className="reply-content">
                <div className="reply-body">
                    {reply.content}
                </div>
                <div className="reply-meta">
                    <span className="reply-date">
                        {reply.createdOn.toLocaleDateString()} at {reply.createdOn.toLocaleTimeString()}
                    </span>
                    {reply.modifiedOn.getTime() !== reply.createdOn.getTime() && (
                        <span className="reply-edited">
                            (edited {reply.modifiedOn.toLocaleDateString()})
                        </span>
                    )}
                </div>
            </div>
            <VoteButtons
                itemId={reply.id}
                voteCount={reply.voteCount ?? 0}
                userVote={reply.userVote || null}
                onVote={onVote}
            />
        </div>
    );
}

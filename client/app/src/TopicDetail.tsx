import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import type { Topic, Reply, VoteType } from './types/forum';
import { VoteButtons } from './components/VoteButtons';
import { ReplyCard } from './components/ReplyCard';
import './TopicDetail.css';

export function TopicDetail() {
    const { id } = useParams<{ id: string }>();
    const [topic, setTopic] = useState<Topic | null>(null);
    const [replies, setReplies] = useState<Reply[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [newReply, setNewReply] = useState('');
    const [submitting, setSubmitting] = useState(false);

    // Mock user ID - in a real app, this would come from authentication
    const currentUserId = 1;

    useEffect(() => {
        if (id) {
            fetchTopicDetails();
            fetchReplies();
        }
    }, [id]);

    const fetchTopicDetails = async () => {
        try {
            const response = await fetch(`http://localhost:8080/topics/${id}`);
            if (!response.ok) {
                throw new Error('Failed to fetch topic');
            }
            const data = await response.json();
            const processedTopic = {
                ...data,
                createdOn: new Date(data.createdOn),
                modifiedOn: new Date(data.modifiedOn),
                voteCount: 0, // Will be fetched separately
                userVote: null
            };
            setTopic(processedTopic);
            
            // Fetch vote count and user vote for the topic
            await Promise.all([
                fetchVoteCount(data.id),
                fetchUserVote(data.id, false)
            ]);
        } catch (error) {
            setError('Error fetching topic details');
            console.error('Error fetching topic:', error);
        }
    };

    const fetchReplies = async () => {
        try {
            const response = await fetch(`http://localhost:8080/replies/topic/${id}`);
            if (!response.ok) {
                throw new Error('Failed to fetch replies');
            }
            const data = await response.json();
            const processedReplies = data.content.map((r: any) => ({
                ...r,
                createdOn: new Date(r.createdOn),
                modifiedOn: new Date(r.modifiedOn),
                voteCount: 0, // Will be fetched separately
                userVote: null
            }));
            setReplies(processedReplies);
            
            // Fetch vote counts and user votes for all replies
            const votePromises = processedReplies.flatMap((reply: Reply) => [
                fetchVoteCount(reply.id),
                fetchUserVote(reply.id, true)
            ]);
            await Promise.all(votePromises);
        } catch (error) {
            console.error('Error fetching replies:', error);
        } finally {
            setLoading(false);
        }
    };

    const fetchVoteCount = async (itemId: number) => {
        try {
            const response = await fetch(`http://localhost:8080/votes/count/${itemId}`);
            if (response.ok) {
                const count = await response.json();
                console.log(`Fetched vote count for item ${itemId}: ${count}`);
                
                // Update the vote count for topic or reply
                // Check if this itemId matches the current topic (from URL parameter)
                if (parseInt(id!) === itemId) {
                    console.log(`Updating topic ${itemId} vote count to ${count}`);
                    setTopic(prev => prev ? { ...prev, voteCount: count } : null);
                } else {
                    console.log(`Updating reply ${itemId} vote count to ${count}`);
                    setReplies(prev => 
                        prev.map(reply => 
                            reply.id === itemId ? { ...reply, voteCount: count } : reply
                        )
                    );
                }
            }
        } catch (error) {
            console.error('Error fetching vote count:', error);
        }
    };

    const fetchUserVote = async (itemId: number, isReply: boolean = false) => {
        try {
            const response = await fetch(`http://localhost:8080/votes/user/${currentUserId}/item/${itemId}`);
            if (response.ok) {
                const userVote = await response.text();
                const voteType = userVote === 'upvote' ? 'upvote' : userVote === 'downvote' ? 'downvote' : null;
                
                console.log(`Fetched user vote for item ${itemId}: ${userVote} -> ${voteType}`);
                
                // Update the user vote for topic or reply
                if (!isReply) {
                    // For topic votes, check if this itemId matches the current topic
                    if (parseInt(id!) === itemId) {
                        console.log(`Updating topic ${itemId} user vote to ${voteType}`);
                        setTopic(prev => prev ? { ...prev, userVote: voteType } : prev);
                    }
                } else {
                    console.log(`Updating reply ${itemId} user vote to ${voteType}`);
                    setReplies(prev => 
                        prev.map(reply => 
                            reply.id === itemId ? { ...reply, userVote: voteType } : reply
                        )
                    );
                }
            }
        } catch (error) {
            console.error('Error fetching user vote:', error);
        }
    };

    const handleVote = async (itemId: number, voteType: VoteType, isReply: boolean = false) => {
        try {
            const response = await fetch('http://localhost:8080/votes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    userId: currentUserId,
                    topicOrReplyId: itemId,
                    voteType: voteType
                }),
            });

            if (!response.ok) {
                throw new Error('Failed to vote');
            }

            const voteResponse = await response.json();
            console.log(`Vote response for item ${itemId}:`, voteResponse);
            
            // After voting, fetch the current user vote state from the backend
            // This ensures we have the correct state after the toggle logic
            await Promise.all([
                fetchVoteCount(itemId),
                fetchUserVote(itemId, isReply)
            ]);
        } catch (error) {
            console.error('Error voting:', error);
        }
    };

    const handleSubmitReply = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!newReply.trim()) return;

        setSubmitting(true);
        try {
            const response = await fetch('http://localhost:8080/replies', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    topicId: parseInt(id!),
                    userId: currentUserId,
                    content: newReply.trim()
                }),
            });

            if (!response.ok) {
                throw new Error('Failed to submit reply');
            }

            const newReplyData = await response.json();
            const processedReply = {
                ...newReplyData,
                createdOn: new Date(newReplyData.createdOn),
                modifiedOn: new Date(newReplyData.modifiedOn),
                voteCount: 0,
                userVote: null
            };

            setReplies(prev => [...prev, processedReply]);
            setNewReply('');
        } catch (error) {
            console.error('Error submitting reply:', error);
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) {
        return (
            <div className="topic-detail-container">
                <div className="loading">Loading topic...</div>
            </div>
        );
    }

    if (error || !topic) {
        return (
            <div className="topic-detail-container">
                <div className="error">{error || 'Topic not found'}</div>
                <Link to="/" className="back-link">← Back to Topics</Link>
            </div>
        );
    }

    return (
        <div className="topic-detail-container">
            <Link to="/" className="back-link">← Back to Topics</Link>
            
            <div className="topic-header">
                <div className="topic-content">
                    <h1 className="topic-title">{topic.title}</h1>
                    <div className="topic-meta">
                        Created on {topic.createdOn.toLocaleDateString()} at {topic.createdOn.toLocaleTimeString()}
                    </div>
                </div>
                <VoteButtons
                    itemId={topic.id}
                    voteCount={topic.voteCount ?? 0}
                    userVote={topic.userVote || null}
                    onVote={(itemId, voteType) => handleVote(itemId, voteType, false)}
                />
            </div>

            <div className="replies-section">
                <h2>Replies ({replies.length})</h2>
                
                <form onSubmit={handleSubmitReply} className="reply-form">
                    <textarea
                        value={newReply}
                        onChange={(e) => setNewReply(e.target.value)}
                        placeholder="Write your reply..."
                        className="reply-input"
                        rows={4}
                        disabled={submitting}
                    />
                    <button 
                        type="submit" 
                        className="reply-submit"
                        disabled={submitting || !newReply.trim()}
                    >
                        {submitting ? 'Submitting...' : 'Post Reply'}
                    </button>
                </form>

                <div className="replies-list">
                    {replies.length === 0 ? (
                        <div className="no-replies">No replies yet. Be the first to reply!</div>
                    ) : (
                        replies.map(reply => (
                            <ReplyCard
                                key={reply.id}
                                reply={reply}
                                onVote={(itemId: number, voteType: VoteType) => handleVote(itemId, voteType, true)}
                            />
                        ))
                    )}
                </div>
            </div>
        </div>
    );
}

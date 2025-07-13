import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Topic, VoteType } from './types/forum';
import { TopicCard } from './components/TopicCard';
import './Topics.css';

export function Topics() {
    const [topics, setTopics] = useState<Topic[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    // Mock user ID - in a real app, this would come from authentication
    const currentUserId = 1;

    useEffect(() => {
        fetchTopics();
    }, []);

    const fetchTopics = async () => {
        try {
            setLoading(true);
            const response = await fetch('http://localhost:8080/topics');
            if (!response.ok) {
                throw new Error('Failed to fetch topics');
            }
            const data = await response.json();
            const processedTopics = data.content.map((t: any) => ({
                ...t,
                createdOn: new Date(t.createdOn),
                modifiedOn: new Date(t.modifiedOn),
                voteCount: 0, // Will be fetched separately
                userVote: null
            }));
            setTopics(processedTopics);
            
            // Fetch vote counts and user votes for all topics
            const votePromises = processedTopics.flatMap((topic: Topic) => [
                fetchVoteCount(topic.id),
                fetchUserVote(topic.id)
            ]);
            await Promise.all(votePromises);
        } catch (error) {
            setError('Error fetching topics');
            console.error('Error fetching topics:', error);
        } finally {
            setLoading(false);
        }
    };

    const fetchVoteCount = async (topicId: number) => {
        try {
            const response = await fetch(`http://localhost:8080/votes/count/${topicId}`);
            if (response.ok) {
                const count = await response.json();
                setTopics(prev => 
                    prev.map(topic => 
                        topic.id === topicId ? { ...topic, voteCount: count } : topic
                    )
                );
            }
        } catch (error) {
            console.error('Error fetching vote count:', error);
        }
    };

    const fetchUserVote = async (topicId: number) => {
        try {
            const response = await fetch(`http://localhost:8080/votes/user/${currentUserId}/item/${topicId}`);
            if (response.ok) {
                const userVote = await response.text();
                const voteType = userVote === 'upvote' ? 'upvote' : userVote === 'downvote' ? 'downvote' : null;
                
                console.log(`Fetched user vote for topic ${topicId}: ${userVote} -> ${voteType}`);
                
                setTopics(prev => 
                    prev.map(topic => 
                        topic.id === topicId ? { ...topic, userVote: voteType } : topic
                    )
                );
            }
        } catch (error) {
            console.error('Error fetching user vote:', error);
        }
    };

    const handleVote = async (topicId: number, voteType: VoteType) => {
        try {
            const response = await fetch('http://localhost:8080/votes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    userId: currentUserId,
                    topicOrReplyId: topicId,
                    voteType: voteType
                }),
            });

            if (!response.ok) {
                throw new Error('Failed to vote');
            }

            const voteResponse = await response.json();
            console.log(`Vote response for topic ${topicId}:`, voteResponse);
            
            // After voting, fetch the current vote count and user vote state from the backend
            // This ensures we have the correct state after the toggle logic
            await Promise.all([
                fetchVoteCount(topicId),
                fetchUserVote(topicId)
            ]);
        } catch (error) {
            console.error('Error voting:', error);
        }
    };

    const handleTopicClick = (topicId: number) => {
        // Navigate to topic details page
        navigate(`/topic/${topicId}`);
    };

    if (loading) {
        return (
            <div className="topics-container">
                <div className="loading">Loading topics...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="topics-container">
                <div className="error">{error}</div>
            </div>
        );
    }

    return (
        <div className="topics-container">

            <div className="topics-list">
                {topics.length === 0 ? (
                    <div className="no-topics">No topics found</div>
                ) : (
                    topics.map(topic => (
                        <TopicCard
                            key={topic.id}
                            topic={topic}
                            onVote={handleVote}
                            onClick={handleTopicClick}
                        />
                    ))
                )}
            </div>
        </div>
    );
}

export default Topics;


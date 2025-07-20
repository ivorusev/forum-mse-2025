import React, { useState, useEffect } from 'react';
import './VoteButtons.css';


const HARDCODED_USER = {
  username: 'testuser',
  isAuthenticated: true // Set to false to test disabled state
};

interface VoteButtonsProps {
  itemId: number;
  itemType: 'topic' | 'reply';
  voteScore: number;
  onVoteChange?: (newScore: number) => void;
}

const VoteButtons: React.FC<VoteButtonsProps> = ({
  itemId,
  itemType,
  voteScore,
  onVoteChange
}) => {
  const [currentScore, setCurrentScore] = useState(voteScore);
  const [userVote, setUserVote] = useState<'up' | 'down' | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  // Fetch user's current vote status when component mounts
  useEffect(() => {
    const fetchUserVote = async () => {
      if (!HARDCODED_USER.isAuthenticated) {
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/votes/${itemType}/${itemId}/user/${HARDCODED_USER.username}`);
        if (response.ok) {
          const voteData = await response.json();
          setUserVote(voteData.isUpvote ? 'up' : 'down');
        } else if (response.status === 404) {
          // User hasn't voted on this item
          setUserVote(null);
        }
      } catch (error) {
        console.error('Error fetching user vote:', error);
        setUserVote(null);
      }
    };

    fetchUserVote();
  }, [itemType, itemId, HARDCODED_USER.username, HARDCODED_USER.isAuthenticated]);

  const handleVote = async (isUpvote: boolean) => {
    // Check if user is authenticated before allowing voting
    if (!HARDCODED_USER.isAuthenticated) {
      console.log('User must be logged in to vote');
      return;
    }

    setIsLoading(true);
    try {
      const username = HARDCODED_USER.username;
      
      const voteRequest = {
        username,
        [itemType === 'topic' ? 'topicId' : 'replyId']: itemId,
        isUpvote
      };

      const response = await fetch('http://localhost:8080/votes', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(voteRequest),
      });

      if (response.ok) {
        // Fetch updated score from server to ensure accuracy
        const scoreResponse = await fetch(`http://localhost:8080/votes/${itemType}/${itemId}/score`);
        if (scoreResponse.ok) {
          const newScore = await scoreResponse.text();
          const score = parseInt(newScore);
          setCurrentScore(score);
          onVoteChange?.(score);
        }
        
        // Update user vote state
        if (response.status === 204) {
          setUserVote(null); // Vote was removed
        } else {
          setUserVote(isUpvote ? 'up' : 'down'); // Vote was added/changed
        }
      }
    } catch (error) {
      console.error('Error voting:', error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="vote-buttons">
      <button
        className={`vote-btn upvote ${userVote === 'up' ? 'active' : ''}`}
        onClick={() => handleVote(true)}
        disabled={isLoading || !HARDCODED_USER.isAuthenticated}
        title={HARDCODED_USER.isAuthenticated ? "Upvote" : "Login required to vote"}
      >
        ↑
      </button>
      
      <span className={`vote-score ${currentScore > 0 ? 'positive' : currentScore < 0 ? 'negative' : ''}`}>
        {currentScore}
      </span>
      
      <button
        className={`vote-btn downvote ${userVote === 'down' ? 'active' : ''}`}
        onClick={() => handleVote(false)}
        disabled={isLoading || !HARDCODED_USER.isAuthenticated}
        title={HARDCODED_USER.isAuthenticated ? "Downvote" : "Login required to vote"}
      >
        ↓
      </button>
    </div>
  );
};

export default VoteButtons;

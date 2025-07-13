export type VoteType = 'upvote' | 'downvote';

export interface Topic {
    id: number;
    title: string;
    createdOn: Date;
    modifiedOn: Date;
    voteCount?: number;
    userVote?: VoteType | null;
}

export interface Reply {
    id: number;
    topicId: number;
    userId: number;
    content: string;
    createdOn: Date;
    modifiedOn: Date;
    voteCount?: number;
    userVote?: VoteType | null;
}

export interface VoteRequest {
    userId: number;
    topicOrReplyId: number;
    voteType: VoteType;
}

export interface VoteResponse {
    topicOrReplyId: number;
    voteCount: number;
}

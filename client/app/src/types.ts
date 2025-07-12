
export type UserResponse = {
    id: number;
    username: string;
    email: string;
    role: string;
};

export type TopicResponse ={
    id: number;
    title: string;
    content: string;
    authorUsername: string;
    createdOn: string;
};

export type ReplyResponse ={
    id: number;
    authorUsername: string;
    replyBody: string;
    createdOn: string;
    topicTitle: string;
};

export type PaginationProps = {
    page: number;
    totalPages: number;
    setPage: (page: number) => void;
    position?: "top" | "bottom";
};
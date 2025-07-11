import React, {type RefObject} from "react";

export type Topic = {
    id: number;
    title: string;
    content?: string;
    createdOn: Date;
    modifiedOn: Date;
};

export type Reply = {
    id: number;
    replyBody: string;
    createdOn: string;
};

export type TopicCardProps = {
    topic: Topic;
    replies: Reply[];
    replyingTo: number | null;
    setReplyingTo: (id: number | null) => void;
    replyContent: string;
    setReplyContent: (v: string) => void;
    sending: boolean;
    success: boolean;
    error: string;
    handleSendReply: (topic: Topic) => void;
    setSuccess: (v: boolean) => void;
    setError: (v: string) => void;
    accordionOpen: Record<number, boolean>;
    setAccordionOpen: React.Dispatch<
        React.SetStateAction<Record<number, boolean>>
    >;
    replyInputRef: RefObject<HTMLTextAreaElement | null>;
};

export type PaginationProps = {
    page: number;
    totalPages: number;
    setPage: (page: number) => void;
    position?: "top" | "bottom";
};
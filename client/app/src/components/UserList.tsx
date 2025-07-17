import React, { useEffect, useState } from 'react';
import Pagination from './Pagination';

type User = {
    id: number;
    username: string;
    email: string;
    createdOn: Date;
    modifiedOn: Date;
};

const UserList: React.FC = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);

    const fetchUsers = async (currentPage: number) => {
        try {
            const res = await fetch(`/users?page=${currentPage}&size=10`, {
                credentials: 'include'
            });
            const data = await res.json();
            console.log("API response:", data);
            setUsers(data.content);
            setTotalPages(data.totalPages);
        } catch (err) {
            console.error("Error fetching users:", err);
        }
    };

    useEffect(() => {
        fetchUsers(page);
    }, [page]);

    return (
        <div>
            <h2>Потребители</h2>
            <ul>
                {users.map(user => (
                    <li key={user.id}>
                        {user.username} ({user.email})
                    </li>
                ))}
            </ul>

            <Pagination
                page={page}
                totalPages={totalPages}
                setPage={setPage}
                position="bottom"
            />
        </div>
    );
};

export default UserList;
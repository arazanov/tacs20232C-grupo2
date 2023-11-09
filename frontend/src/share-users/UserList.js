import {Button, ButtonGroup, Table} from "reactstrap";
import React from "react";

export function UserList({ users, orderId, setUsers }) {

    function remove(id) {
        let token = localStorage.getItem('token');
        fetch("/orders/" + orderId + "/users/" + id, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        }).then(() => {
            setUsers(users.filter(i => i.id !== id))
        });
    }

    return <Table className="mt-4">
        <thead>
        <tr>
            <th width="30%">Nombre de usuario</th>
            <th width="30%">Email</th>
            <th width="40%">Acciones</th>
        </tr>
        </thead>
        <tbody>
        {
            users.map(user => {
                return <tr key={user.id}>
                    <td style={{whiteSpace: 'nowrap'}}>{user.username}</td>
                    <td>{user.email}</td>
                    <td>
                        <ButtonGroup>
                            <Button size="sm" color="danger" onClick={() => remove(user.id)}>
                                Eliminar
                            </Button>
                        </ButtonGroup>
                    </td>
                </tr>
            })
        }
        </tbody>
    </Table>
}
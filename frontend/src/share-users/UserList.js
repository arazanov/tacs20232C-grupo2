import {Button, ButtonGroup, Table} from "reactstrap";
import React from "react";
import {useAuth} from "../AuthContext";
import {useNavigate} from "react-router-dom";

export function UserList({users, orderId, setUsers}) {
    const {token} = useAuth();
    const navigate = useNavigate();

    function remove(id) {
        fetch("/orders/" + orderId + "/users/" + id, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        }).then(response => {
            if (!response.ok) {
                return response.text().then(body => {
                    throw new Error(body);
                })
            }
            setUsers(users.filter(i => i.id !== id));
        }).catch(err => {
            console.log(err);
            alert("Pedido cerrado");
            navigate("/orders");
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
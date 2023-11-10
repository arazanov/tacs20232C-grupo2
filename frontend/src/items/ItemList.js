import {Button, ButtonGroup, Table} from "reactstrap";
import React from "react";

export function ItemList({ items, orderId, setItems }) {

    function remove(id) {
        let token = localStorage.getItem('token');
        fetch("/items/" + id, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        }).then(() => {
            setItems(items.filter(i => i.id !== id))
        });
    }

    return <Table className="mt-4">
        <thead>
        <tr>
            <th width="30%">Ítem</th>
            <th width="15%">Cantidad</th>
            <th width="15%">Unidad</th>
            <th width="40%">Acciones</th>
        </tr>
        </thead>
        <tbody>
        {
            items.map(item => {
                return <tr key={item.id}>
                    <td style={{whiteSpace: 'nowrap'}}>{item.description}</td>
                    <td>{item.quantity}</td>
                    <td>{item.unit}</td>
                    <td>
                        <ButtonGroup>
                            <Button size="sm" color="primary"
                                    href={"/orders/" + orderId + "/items/" + item.id}>
                                Editar
                            </Button>
                            <Button size="sm" color="danger" onClick={() => remove(item.id)}>
                                Eliminar
                            </Button>
                        </ButtonGroup>
                    </td>
                </tr>
            })
        }
        </tbody>
    </Table>;
}
import React, {useEffect, useState} from 'react';
import {Button, ButtonGroup, Container, FormGroup, Table} from 'reactstrap';
import AppNavbar from '../AppNavbar';

export default function OrderList({ token }) {
    const [orders, setOrders] = useState([]);
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));

    useEffect(() => {


        fetch('/orders', {
            method: "GET",
            headers: { "Authorization": "Bearer " + currentUser.token }
        })
            .then(response => response.json())
            .then(data => setOrders(data));
    }, [orders, currentUser]);

    function remove(id) {
        fetch(`/orders/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() =>
            setOrders(o => o.filter(order => order.id !== id))
        );
    }

    function handleChange(id, value) {
        fetch(`/orders/${id}`, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + currentUser.token
            },
            body: JSON.stringify({ closed: value })
        })
            .then((response) => response.json())
            .then((data) => {
                let index = orders.findIndex(o => o.id === id);
                orders[index] = data;
            });
    }

    const orderList = orders.map(order => {
        return <tr key={order.id}>
            <td style={{whiteSpace: 'nowrap'}}>{order.description}</td>
            <td>{order.closed ? "Cerrado" : "Abierto"}</td>
            <td>
                <ButtonGroup>
                    <Button size="sm" color="primary" href={`/orders/${order.id}`}>Editar</Button>
                    <Button size="sm" color="secondary" onClick={() => handleChange(order.id, !order.closed)}>
                        {order.closed ? "Abrir" : "Cerrar"}
                    </Button>
                    <Button size="sm" color="danger" onClick={() => remove(order.id)}>Eliminar</Button>
                </ButtonGroup>
            </td>
        </tr>
    });

    return (
        <div>
            <AppNavbar/>
            <Container fluid>
                <h3 style={{ paddingTop: 50 }}>Pedidos</h3>
                <Table className="mt-4">
                    <thead>
                        <tr>
                            <th width="30%">Nombre</th>
                            <th width="30%">Estado</th>
                            <th width="40%">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orderList}
                    </tbody>
                </Table>
                <FormGroup style={{ paddingTop: 30 }}>
                    <Button color="primary" type="submit">Crear orden</Button>{' '}
                    <Button color="secondary" href="/orders">Cerrar sesión</Button>
                </FormGroup>
            </Container>
        </div>
    );
}
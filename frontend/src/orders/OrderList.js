import React, {useEffect, useState} from 'react';
import {Button, ButtonGroup, Container, FormGroup, Table} from 'reactstrap';
import AppNavbar from '../AppNavbar';
import { Link } from 'react-router-dom';

export default function OrderList() {
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        fetch('/orders')
            .then(response => response.json())
            .then(data => setOrders(data));
    }, [orders]);

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
        fetch(`/orders/${id}/1`, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
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
            <td>{order.closed ? "Closed" : "Open"}</td>
            <td>
                <ButtonGroup>
                    <Link to={`/orders/${order.id}`}>
                        <Button size="sm" color="primary">Edit</Button>
                    </Link>
                    <Button size="sm" color="secondary" onClick={() => handleChange(order.id, !order.closed)}>
                        {order.closed ? "Open" : "Close"}
                    </Button>
                    <Button size="sm" color="danger" onClick={() => remove(order.id)}>Delete</Button>
                </ButtonGroup>
            </td>
        </tr>
    });

    return (
        <div>
            <AppNavbar/>
            <Container fluid>
                <h3 style={{ paddingTop: 50 }}>Orders</h3>
                <Table className="mt-4">
                    <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="30%">State</th>
                            <th width="40%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orderList}
                    </tbody>
                </Table>
                <FormGroup style={{paddingTop: 20}}>
                    <Link to={"/orders/new"}><Button color="success">Add Order</Button></Link>
                </FormGroup>
            </Container>
        </div>
    );
}
import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {Button, ButtonGroup, Container, Form, FormGroup, Input, Label, Table, Row, Col} from 'reactstrap';
import AppNavbar from '../AppNavbar';

export default function OrderEdit() {
    const { id } = useParams();
    const [order, setOrder] = useState({ description: '', items: [], users: [] });
    const navigate = useNavigate();

    useEffect(() => {
        fetch(`/orders/${id}`)
            .then((response) => response.json())
            .then((data) => setOrder(data));
    }, [id]);

    function handleOrderChange(event) {
        const name = event.target.name;
        const value = event.target.value;

        let updatedOrder = order;
        updatedOrder[name] = value;
        setOrder(updatedOrder);
    }

    function handleItemChange(e, itemId) {
        let updatedOrder = order;
        updatedOrder.items[itemId - 1][e.target.name] = e.target.value;
        setOrder(updatedOrder);
        console.log(order.items[itemId - 1]);
    }

    function handleSubmit(event) {
        event.preventDefault();

        fetch(('/orders/' + id + '/1'), {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(order),
        })
            .then(response => response.json())
            .then(data => console.log(data));

        navigate('/orders');
    }

    const userList = order.users.map(i => {
        return (
            <tr key={i.id}>
                <td style={{whiteSpace: 'nowrap'}}>{i.username}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="danger">Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        )
    });

    const itemList = order.items.map(i => {
        return (
            <Row key={i.id}>
                <Col md={5}>
                    <FormGroup>
                        <Input name="description" type="text" bsSize="sm" defaultValue={i.description}
                               onChange={(e) => handleItemChange(e, i.id)}/>
                    </FormGroup>
                </Col>
                <Col md={2}>
                    <FormGroup>
                        <Input name="quantity" type="number" bsSize="sm" defaultValue={i.quantity}
                               onChange={(e) => handleItemChange(e, i.id)}/>
                    </FormGroup>
                </Col>
                <Col md={3}>
                    <Button size="sm" color="danger">Delete</Button>
                </Col>
            </Row>
        )
    });

    return (
        <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 20}}>Edit order</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="description">Description</Label>
                    <Input type="text" name="description" id="description" defaultValue={order.description || ''}
                           onChange={handleOrderChange} autoComplete="description"/>
                </FormGroup>
                <h3 style={{paddingTop: 30, paddingBottom: 20}}>Items</h3>
                {itemList}
                <h3 style={{paddingTop: 30}}>Users</h3>
                <Table className="mt-4">
                    <thead>
                        <tr>
                            <th width="60%">Name</th>
                            <th width="40%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {userList}
                    </tbody>
                </Table>
                <FormGroup style={{paddingTop: 30}}>
                    <Button color="primary" type="submit">Save</Button>{' '}
                    <Button color="secondary" href="/orders">Cancel</Button>
                </FormGroup>
            </Form>
        </Container>
    </div>
    );

}
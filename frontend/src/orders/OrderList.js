import React, { Component } from 'react';
import {Button, ButtonGroup, Container, FormGroup, Table} from 'reactstrap';
import AppNavbar from '../AppNavbar';
import { Link } from 'react-router-dom';

class OrderList extends Component {

    constructor(props) {
        super(props);
        this.state = {orders: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('/orders')
            .then(response => response.json())
            .then(data => this.setState({orders: data}));
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (
            this.state.orders !== prevState.orders
        ) {
            fetch('/orders')
                .then(response => response.json())
                .then(data => this.setState({orders: data}));
        }
    }

    async remove(id) {
        await fetch(`/orders/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedOrders = [...this.state.orders].filter(i => i.id !== id);
            this.setState({orders: updatedOrders});
        });
    }

    async close(id) {
        await fetch(`/orders/${id}/1`, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                closed: true
            })
        })
            .then((response) => response.json())
            .then((data) => console.log(data));
    }

    async open(id) {
        await fetch(`/orders/${id}/1`, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                closed: false
            })
        })
            .then((response) => response.json())
            .then((data) => console.log(data));
    }

    render() {
        const {orders, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const orderList = orders.map(order => {
            return <tr key={order.id}>
                <td style={{whiteSpace: 'nowrap'}}>{order.description}</td>
                <td>{order.closed ? "closed" : "open"}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/orders/" + order.id}>Edit</Button>
                        <Button size="sm" color="secondary" onClick={() => order.closed ? this.open(order.id) : this.close(order.id)}>{order.closed ? "Open" : "Close"}</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(order.id)}>Delete</Button>
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
                        <Button color="success" tag={Link} to="/orders/new">Add Order</Button>
                    </FormGroup>
                </Container>
            </div>
        );
    }

}
export default OrderList;
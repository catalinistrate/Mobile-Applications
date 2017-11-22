import React from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';

export default class Comment extends React.Component {
    render() {
        return (
            <View key={this.props.keyval} style={styles.comment}>
                <Text style={styles.commentText}>
                    {this.props.val.date} 
                </Text>
                <Text style={styles.commentText}>
                    {this.props.val.comment}
                </Text>

                <TouchableOpacity onPress={this.props.deleteMethod} style={styles.commentDeleted}>
                    <Text style={styles.commentDeletedText}>X</Text>
                </TouchableOpacity>
            </View>

        );
    }
}

const styles = StyleSheet.create({
    comment: {
        position: 'relative',
        padding: 20,
        paddingRight:100,
        borderBottomWidth: 2,
        borderBottomColor: '#ededed',
    },
    commentText: {
        paddingLeft: 20,
        borderLeftWidth: 10,
        color: '#000000',
        borderLeftColor: '#e91e63',
    },
    commentDeleted: {
        position: 'absolute',
        justifyContent: 'space-around',
        alignItems: 'center',
        backgroundColor: '#2980b9',
        padding: 10,
        top: 10,
        bottom: 10,
        right: 10
    },
    commentDeletedText: {
        color: '#ffffff'
    }});﻿
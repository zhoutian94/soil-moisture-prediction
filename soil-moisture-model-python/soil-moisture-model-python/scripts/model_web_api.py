import torch
from flask import Flask, request, jsonify

from model import LSTMModel  # 导入LSTMModel类

# 创建 Flask 应用
app = Flask(__name__)

# 定义输入特征的尺寸等
input_size = 4  # 假设有4个特征（pre, gts, sm, differential_delay）
hidden_size = 5  # 隐藏层单元数
num_layers = 1  # LSTM层数
time_step = 7  # 时间步数，假设是7天数据

# 加载训练好的模型
model = LSTMModel(input_size=input_size, hidden_size=hidden_size, num_layers=num_layers)
model.load_state_dict(torch.load('sm-model.pth', weights_only=True))  # 加载保存的模型参数
model.eval()  # 切换到评估模式

#%%
import pandas as pd
import joblib
import numpy as np

# 加载缩放器
scaler = joblib.load('scaler.pkl')

# 定义预测接口
@app.route('/predict', methods=['POST'])
def predict():
    try:
        # 从请求中获取 JSON 数据
        data = request.get_json()

        # 提取特征数据
        pre = data['pre']
        gts = data['gts']
        sm = data['sm']
        differential_delay = data['differential_delay']

        # 将数据合并成一个二维数组（每个时间点的数据为一个样本）
        features = {
                        'pre': pre,
                        'gts': gts,
                        'sm': sm,
                        'differential_delay': differential_delay
                    }

        # 将输入数据转换为 DataFrame
        latest_data = pd.DataFrame(features)
        print(latest_data)

        # 对输入数据进行标准化
        latest_data_scaled = scaler.transform(latest_data)

        # 进行预测
        predictions = []
        for _ in range(7):  # 预测未来7天
            # 转换为适合模型输入的格式： [batch_size, time_step, input_size]
            latest_tensor = torch.FloatTensor(latest_data_scaled).view(1, time_step, -1)
            pred = model(latest_tensor)
            y_pred_temp = scaler.inverse_transform(
                np.concatenate((np.zeros((pred.shape[0], 2)), pred.detach().numpy(), np.zeros((pred.shape[0], 1))),
                               axis=1))[:, 2]
            predictions.append(y_pred_temp.item())

            # 更新latest_data，添加新预测值
            new_row = np.array([pred.item(), 0, 0, 0])  # 用0填充其他列
            latest_data_scaled = np.append(latest_data_scaled[1:], [new_row], axis=0)  # 更新输入数据

        # 返回预测结果
        return jsonify({'predictions': predictions})

    except Exception as e:
        return jsonify({'error': str(e)}), 400

# 启动 Flask 应用
if __name__ == '__main__':
    app.run(debug=True)

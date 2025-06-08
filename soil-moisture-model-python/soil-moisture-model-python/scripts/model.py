# 构建模型
import torch.nn as nn

# nn.Module是PyTorch中所有神经网络模块的基类
class LSTMModel(nn.Module):
    # input_size: 输入特征数；hidden_size: 隐藏层单元数；num_layers: 可构建堆叠的LSTM层数
    def __init__(self, input_size , hidden_size, num_layers):
        # 调用父类的构造函数，确保父类方法、属性能正常被调用
        super(LSTMModel, self).__init__()
        # 构建一个LSTM层（batch_first用于决定张量的第一个维度是否表示批次大小
        self.lstm = nn.LSTM(input_size, hidden_size, num_layers, batch_first=True)
        # 定义一个全连接层（线性层），用于将 LSTM 输出的隐藏状态转化为最终的预测值。每个输入序列只对应1个输出值
        self.fc = nn.Linear(hidden_size, 1)

    #定义向前传播过程（计算输出的过程）
    def forward(self, x):
        #获取out（所有时间步的隐藏状态）和隐藏状态（_表示未使用）。
        out, _ = self.lstm(x)
        #从 LSTM 层的输出中提取最后一个时间步的隐藏状态，并将其传递给全连接层（self.fc）以生成最终的预测值
        out = self.fc(out[:, -1, :])
        return out